package com.loanmanagement.loanmanagementbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanmanagement.loanmanagementbackend.model.CreateRepaymentRequest;
import com.loanmanagement.loanmanagementbackend.model.Customer;
import com.loanmanagement.loanmanagementbackend.model.Loan;
import com.loanmanagement.loanmanagementbackend.model.LoanStatus;
import com.loanmanagement.loanmanagementbackend.model.Role;
import com.loanmanagement.loanmanagementbackend.repository.CustomerRepository;
import com.loanmanagement.loanmanagementbackend.repository.LoanRepository;
import com.loanmanagement.loanmanagementbackend.security.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class RepaymentControllerTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;
    private Customer savedCustomer;
    private String customerToken;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        Customer customer = new Customer();
        customer.setName("Charlie Brown");
        customer.setEmail("charlie@example.com");
        savedCustomer = customerRepository.save(customer);

        customerToken = jwtUtils.generateToken("charlieUser", Role.ROLE_CUSTOMER.name());
    }

    @Test
    public void testRepaymentPendingLoanFails() throws Exception {
        Loan loan = new Loan();
        loan.setLoanType("PERSONAL");
        loan.setAmount(5000.0);
        loan.setTenure(6);
        loan.setCustomer(savedCustomer);
        loan.setStatus(LoanStatus.PENDING);
        Loan savedLoan = loanRepository.save(loan);

        CreateRepaymentRequest request = new CreateRepaymentRequest();
        request.setLoanId(savedLoan.getId());
        request.setAmount(1000.0);
        request.setPaymentDate(LocalDate.now());

        mockMvc.perform(post("/api/repayments")
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Repayments can only be processed for APPROVED or ACTIVE loans"));
    }

    @Test
    public void testRepaymentFullAmountClosesLoan() throws Exception {
        Loan loan = new Loan();
        loan.setLoanType("PERSONAL");
        loan.setAmount(5000.0);
        loan.setTenure(6);
        loan.setCustomer(savedCustomer);
        loan.setStatus(LoanStatus.APPROVED);
        Loan savedLoan = loanRepository.save(loan);

        CreateRepaymentRequest request = new CreateRepaymentRequest();
        request.setLoanId(savedLoan.getId());
        request.setAmount(5000.0);
        request.setPaymentDate(LocalDate.now());

        mockMvc.perform(post("/api/repayments")
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value(5000.0));

        mockMvc.perform(get("/api/loans/" + savedLoan.getId())
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CLOSED"));
    }
}
