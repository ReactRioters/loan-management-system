package com.loanmanagement.loanmanagementbackend.controller;

import com.loanmanagement.loanmanagementbackend.model.LoanStatus;
import com.loanmanagement.loanmanagementbackend.model.LoanDashboardSummary;

import com.loanmanagement.loanmanagementbackend.model.Loan;
import com.loanmanagement.loanmanagementbackend.service.LoanService;
import com.loanmanagement.loanmanagementbackend.model.LoanEmiResponse;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @GetMapping
    public List<Loan> getLoans() {
        return loanService.getLoans();
    }

    @GetMapping("/dashboard/summary")
    public LoanDashboardSummary getDashboardSummary() {
        return loanService.getDashboardSummary();
    }

    @GetMapping("/{id}")
    public Loan getLoanById(@PathVariable Long id) {
        return loanService.getLoanById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Loan createLoan(@Valid @RequestBody Loan loan) {
        return loanService.createLoan(loan);
    }

    @PutMapping("/{id}")
    public Loan updateLoan(
            @PathVariable Long id,
            @Valid @RequestBody Loan loan) {
        return loanService.updateLoan(id, loan);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
    }

    @PutMapping("/{id}/approve")
    public Loan approveLoan(@PathVariable Long id) {
        return loanService.approveLoan(id);
    }

    @PutMapping("/{id}/reject")
    public Loan rejectLoan(@PathVariable Long id) {
        return loanService.rejectLoan(id);
    }

    @PutMapping("/{id}/cancel")
    public Loan cancelLoan(@PathVariable Long id) {
        return loanService.cancelLoan(id);
    }

    @GetMapping("/customer/{customerId}")
    public List<Loan> getLoansByCustomerId(
            @PathVariable Long customerId) {
        return loanService.getLoansByCustomerId(customerId);
    }

    @GetMapping("/search")
    public Page<Loan> searchLoans(
            @RequestParam(required = false) String loanType,
            @RequestParam(required = false) LoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        return loanService.searchLoans(
                loanType,
                status,
                page,
                size,
                sortBy,
                direction);
    }

    @GetMapping("/{id}/emi")
    public LoanEmiResponse calculateEmi(
            @PathVariable Long id,
            @RequestParam double annualInterestRate) {

        return loanService.calculateEmi(
                id,
                annualInterestRate);
    }
}