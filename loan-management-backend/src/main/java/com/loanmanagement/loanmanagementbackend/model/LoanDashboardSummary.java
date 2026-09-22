package com.loanmanagement.loanmanagementbackend.model;

public class LoanDashboardSummary {

    private long totalLoans;
    private long pendingLoans;
    private long approvedLoans;
    private long rejectedLoans;
    private double totalLoanAmount;
    private double totalApprovedLoanAmount;

    public LoanDashboardSummary() {
    }

    public LoanDashboardSummary(
            long totalLoans,
            long pendingLoans,
            long approvedLoans,
            long rejectedLoans,
            double totalLoanAmount,
            double totalApprovedLoanAmount) {

        this.totalLoans = totalLoans;
        this.pendingLoans = pendingLoans;
        this.approvedLoans = approvedLoans;
        this.rejectedLoans = rejectedLoans;
        this.totalLoanAmount = totalLoanAmount;
        this.totalApprovedLoanAmount = totalApprovedLoanAmount;
    }

    public long getTotalLoans() {
        return totalLoans;
    }

    public long getPendingLoans() {
        return pendingLoans;
    }

    public long getApprovedLoans() {
        return approvedLoans;
    }

    public long getRejectedLoans() {
        return rejectedLoans;
    }

    public double getTotalLoanAmount() {
        return totalLoanAmount;
    }

    public double getTotalApprovedLoanAmount() {
        return totalApprovedLoanAmount;
    }
}