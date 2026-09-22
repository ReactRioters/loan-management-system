package com.loanmanagement.loanmanagementbackend.model;

import java.util.List;

public class LoanEmiResponse {

    private Long loanId;
    private double principalAmount;
    private int tenureMonths;
    private double annualInterestRate;
    private double monthlyEmi;
    private double totalPayableAmount;
    private double totalInterest;
    private List<RepaymentScheduleItem> repaymentSchedule;

    public LoanEmiResponse() {
    }

    public LoanEmiResponse(
            Long loanId,
            double principalAmount,
            int tenureMonths,
            double annualInterestRate,
            double monthlyEmi,
            double totalPayableAmount,
            double totalInterest,
            List<RepaymentScheduleItem> repaymentSchedule) {

        this.loanId = loanId;
        this.principalAmount = principalAmount;
        this.tenureMonths = tenureMonths;
        this.annualInterestRate = annualInterestRate;
        this.monthlyEmi = monthlyEmi;
        this.totalPayableAmount = totalPayableAmount;
        this.totalInterest = totalInterest;
        this.repaymentSchedule = repaymentSchedule;
    }

    public Long getLoanId() {
        return loanId;
    }

    public double getPrincipalAmount() {
        return principalAmount;
    }

    public int getTenureMonths() {
        return tenureMonths;
    }

    public double getAnnualInterestRate() {
        return annualInterestRate;
    }

    public double getMonthlyEmi() {
        return monthlyEmi;
    }

    public double getTotalPayableAmount() {
        return totalPayableAmount;
    }

    public double getTotalInterest() {
        return totalInterest;
    }

    public List<RepaymentScheduleItem> getRepaymentSchedule() {
        return repaymentSchedule;
    }
}