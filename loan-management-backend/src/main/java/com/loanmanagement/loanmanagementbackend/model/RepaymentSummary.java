package com.loanmanagement.loanmanagementbackend.model;

public class RepaymentSummary {

    private Long loanId;
    private double totalLoanAmount;
    private double totalPaidAmount;
    private double remainingAmount;
    private long totalPayments;

    public RepaymentSummary() {
    }

    public RepaymentSummary(
            Long loanId,
            double totalLoanAmount,
            double totalPaidAmount,
            double remainingAmount,
            long totalPayments) {

        this.loanId = loanId;
        this.totalLoanAmount = totalLoanAmount;
        this.totalPaidAmount = totalPaidAmount;
        this.remainingAmount = remainingAmount;
        this.totalPayments = totalPayments;
    }

    public Long getLoanId() {
        return loanId;
    }

    public double getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public double getTotalPaidAmount() {
        return totalPaidAmount;
    }

    public double getRemainingAmount() {
        return remainingAmount;
    }

    public long getTotalPayments() {
        return totalPayments;
    }
}