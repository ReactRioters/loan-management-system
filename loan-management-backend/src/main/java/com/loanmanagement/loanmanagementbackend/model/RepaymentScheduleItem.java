package com.loanmanagement.loanmanagementbackend.model;

public class RepaymentScheduleItem {

    private int month;
    private double emi;
    private double principalPaid;
    private double interestPaid;
    private double remainingBalance;

    public RepaymentScheduleItem() {
    }

    public RepaymentScheduleItem(
            int month,
            double emi,
            double principalPaid,
            double interestPaid,
            double remainingBalance) {

        this.month = month;
        this.emi = emi;
        this.principalPaid = principalPaid;
        this.interestPaid = interestPaid;
        this.remainingBalance = remainingBalance;
    }

    public int getMonth() {
        return month;
    }

    public double getEmi() {
        return emi;
    }

    public double getPrincipalPaid() {
        return principalPaid;
    }

    public double getInterestPaid() {
        return interestPaid;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }
}