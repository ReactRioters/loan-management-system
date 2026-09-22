package com.loanmanagement.loanmanagementbackend.service;

import com.loanmanagement.loanmanagementbackend.model.Customer;
import com.loanmanagement.loanmanagementbackend.repository.CustomerRepository;
import com.loanmanagement.loanmanagementbackend.repository.LoanRepository;
import com.loanmanagement.loanmanagementbackend.model.CustomerDashboardSummary;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    private final LoanRepository loanRepository;

    public CustomerService(
        CustomerRepository customerRepository,
        LoanRepository loanRepository) {

    this.customerRepository = customerRepository;
    this.loanRepository = loanRepository;
}

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public List<Customer> getCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found"));
    }

    public CustomerDashboardSummary getDashboardSummary() {

        long totalCustomers = customerRepository.count();

        long customersWithLoans = customerRepository.countCustomersWithLoans();

        long customersWithoutLoans = customerRepository.countCustomersWithoutLoans();

        long totalLoanApplications = loanRepository.count();

        return new CustomerDashboardSummary(
                totalCustomers,
                customersWithLoans,
                customersWithoutLoans,
                totalLoanApplications);
    }
}