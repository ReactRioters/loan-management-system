package com.loanmanagement.loanmanagementbackend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Loan type is required")
    private String loanType;

    @Min(value = 1, message = "Loan amount must be greater than 0")
    private double amount;

    @Min(value = 1, message = "Tenure must be greater than 0")
    private int tenure;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    public Loan() {
    }

    public Loan(
            Long id,
            String loanType,
            double amount,
            int tenure,
            LoanStatus status
    ) {
        this.id = id;
        this.loanType = loanType;
        this.amount = amount;
        this.tenure = tenure;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getLoanType() {
        return loanType;
    }

    public double getAmount() {
        return amount;
    }

    public int getTenure() {
        return tenure;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}