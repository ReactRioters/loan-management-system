package com.loanmanagement.loanmanagementbackend;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loanmanagement.loanmanagementbackend.model.Customer;
import com.loanmanagement.loanmanagementbackend.model.Role;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class CustomerControllerTest {

    @Autowired
    private WebApplicationContext context;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Autowired
    private JwtUtils jwtUtils;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    public void testCustomerCreationAndFetch() throws Exception {
        String adminToken = jwtUtils.generateToken("adminUser", Role.ROLE_ADMIN.name());

        Customer customer = new Customer();
        customer.setName("Alice Smith");
        customer.setEmail("alice@example.com");

        String responseContent = mockMvc.perform(post("/api/customers")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Alice Smith"))
                .andReturn().getResponse().getContentAsString();

        Customer createdCustomer = objectMapper.readValue(responseContent, Customer.class);

        mockMvc.perform(get("/api/customers/" + createdCustomer.getId())
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    public void testGetAllCustomersForbiddenForCustomerRole() throws Exception {
        String customerToken = jwtUtils.generateToken("customerUser", Role.ROLE_CUSTOMER.name());

        mockMvc.perform(get("/api/customers")
                        .header("Authorization", "Bearer " + customerToken))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testGetAllCustomersAllowedForAdminRole() throws Exception {
        String adminToken = jwtUtils.generateToken("adminUser", Role.ROLE_ADMIN.name());

        mockMvc.perform(get("/api/customers")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }
}
