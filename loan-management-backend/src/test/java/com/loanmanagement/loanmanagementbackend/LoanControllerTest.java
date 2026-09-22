package com.loanmanagement.loanmanagementbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class LoanControllerTest {

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
    private String adminToken;
    private String customerToken;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        Customer customer = new Customer();
        customer.setName("Bob Miller");
        customer.setEmail("bob@example.com");
        savedCustomer = customerRepository.save(customer);

        adminToken = jwtUtils.generateToken("adminUser", Role.ROLE_ADMIN.name());
        customerToken = jwtUtils.generateToken("customerUser", Role.ROLE_CUSTOMER.name());
    }

    @Test
    public void testCreateLoanSuccess() throws Exception {
        Loan loan = new Loan();
        loan.setLoanType("PERSONAL");
        loan.setAmount(10000.0);
        loan.setTenure(12);
        loan.setCustomer(savedCustomer);

        mockMvc.perform(post("/api/loans")
                        .header("Authorization", "Bearer " + customerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loan)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.amount").value(10000.0));
    }

    @Test
    public void testApproveLoanByAdminSuccess() throws Exception {
        Loan loan = new Loan();
        loan.setLoanType("HOME");
        loan.setAmount(50000.0);
        loan.setTenure(24);
        loan.setCustomer(savedCustomer);
        loan.setStatus(LoanStatus.PENDING);
        Loan savedLoan = loanRepository.save(loan);

        mockMvc.perform(put("/api/loans/" + savedLoan.getId() + "/approve")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    @Test
    public void testApproveLoanForbiddenForCustomerRole() throws Exception {
        Loan loan = new Loan();
        loan.setLoanType("HOME");
        loan.setAmount(50000.0);
        loan.setTenure(24);
        loan.setCustomer(savedCustomer);
        loan.setStatus(LoanStatus.PENDING);
        Loan savedLoan = loanRepository.save(loan);

        mockMvc.perform(put("/api/loans/" + savedLoan.getId() + "/approve")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCancelLoanSuccess() throws Exception {
        Loan loan = new Loan();
        loan.setLoanType("CAR");
        loan.setAmount(15000.0);
        loan.setTenure(12);
        loan.setCustomer(savedCustomer);
        loan.setStatus(LoanStatus.PENDING);
        Loan savedLoan = loanRepository.save(loan);

        mockMvc.perform(put("/api/loans/" + savedLoan.getId() + "/cancel")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    public void testCalculateEmiSuccess() throws Exception {
        Loan loan = new Loan();
        loan.setLoanType("PERSONAL");
        loan.setAmount(12000.0);
        loan.setTenure(12);
        loan.setCustomer(savedCustomer);
        loan.setStatus(LoanStatus.APPROVED);
        Loan savedLoan = loanRepository.save(loan);

        mockMvc.perform(get("/api/loans/" + savedLoan.getId() + "/emi?annualInterestRate=10.0")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.monthlyEmi").exists())
                .andExpect(jsonPath("$.totalPayableAmount").exists())
                .andExpect(jsonPath("$.repaymentSchedule").isArray());
    }
}
