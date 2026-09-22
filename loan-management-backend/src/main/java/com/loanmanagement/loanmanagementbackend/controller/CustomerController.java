package com.loanmanagement.loanmanagementbackend.controller;

import com.loanmanagement.loanmanagementbackend.model.Customer;
import com.loanmanagement.loanmanagementbackend.service.CustomerService;
import com.loanmanagement.loanmanagementbackend.model.CustomerDashboardSummary;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(
            @Valid @RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id);
    }

    @GetMapping("/dashboard/summary")
    public CustomerDashboardSummary getDashboardSummary() {
        return customerService.getDashboardSummary();
    }
}