package com.loanmanagement.loanmanagementbackend.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateRepaymentRequest {

    @NotNull(message = "Repayment amount is required")
    @Min(value = 1, message = "Repayment amount must be at least 1")
    private Double amount;

    private LocalDate paymentDate;

    @NotNull(message = "Loan ID is required")
    private Long loanId;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }
}