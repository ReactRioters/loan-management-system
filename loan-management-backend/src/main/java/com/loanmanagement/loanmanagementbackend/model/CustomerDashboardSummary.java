package com.loanmanagement.loanmanagementbackend.model;

public class CustomerDashboardSummary {

    private long totalCustomers;
    private long customersWithLoans;
    private long customersWithoutLoans;
    private long totalLoanApplications;

    public CustomerDashboardSummary() {
    }

    public CustomerDashboardSummary(
            long totalCustomers,
            long customersWithLoans,
            long customersWithoutLoans,
            long totalLoanApplications) {

        this.totalCustomers = totalCustomers;
        this.customersWithLoans = customersWithLoans;
        this.customersWithoutLoans = customersWithoutLoans;
        this.totalLoanApplications = totalLoanApplications;
    }

    public long getTotalCustomers() {
        return totalCustomers;
    }

    public long getCustomersWithLoans() {
        return customersWithLoans;
    }

    public long getCustomersWithoutLoans() {
        return customersWithoutLoans;
    }

    public long getTotalLoanApplications() {
        return totalLoanApplications;
    }
}