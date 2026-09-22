package com.loanmanagement.loanmanagementbackend.service;

import com.loanmanagement.loanmanagementbackend.model.Customer;
import com.loanmanagement.loanmanagementbackend.model.Loan;
import com.loanmanagement.loanmanagementbackend.model.LoanStatus;
import com.loanmanagement.loanmanagementbackend.repository.CustomerRepository;
import com.loanmanagement.loanmanagementbackend.repository.LoanRepository;
import com.loanmanagement.loanmanagementbackend.model.LoanDashboardSummary;
import com.loanmanagement.loanmanagementbackend.model.LoanEmiResponse;
import com.loanmanagement.loanmanagementbackend.model.RepaymentScheduleItem;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loanRepository;
    private final CustomerRepository customerRepository;

    public LoanService(
            LoanRepository loanRepository,
            CustomerRepository customerRepository) {
        this.loanRepository = loanRepository;
        this.customerRepository = customerRepository;
    }

    public List<Loan> getLoans() {
        return loanRepository.findAll();
    }

    public Loan getLoanById(Long id) {
        return loanRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Loan not found"));
    }

    public Loan createLoan(Loan loan) {

        if (loan.getCustomer() == null ||
                loan.getCustomer().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Customer ID is required");
        }

        Long customerId = loan.getCustomer().getId();

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Customer not found"));

        loan.setCustomer(customer);
        loan.setStatus(LoanStatus.PENDING);

        return loanRepository.save(loan);
    }

    public Loan updateLoan(Long id, Loan updatedLoan) {

        Loan existingLoan = getLoanById(id);

        existingLoan.setLoanType(updatedLoan.getLoanType());
        existingLoan.setAmount(updatedLoan.getAmount());
        existingLoan.setTenure(updatedLoan.getTenure());

        return loanRepository.save(existingLoan);
    }

    public void deleteLoan(Long id) {
        Loan loan = getLoanById(id);
        loanRepository.delete(loan);
    }

    public Loan approveLoan(Long id) {

        Loan loan = getLoanById(id);

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only pending loans can be approved");
        }

        loan.setStatus(LoanStatus.APPROVED);

        return loanRepository.save(loan);
    }

    public Loan rejectLoan(Long id) {

        Loan loan = getLoanById(id);

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only pending loans can be rejected");
        }

        loan.setStatus(LoanStatus.REJECTED);

        return loanRepository.save(loan);
    }

    public Loan cancelLoan(Long id) {

        Loan loan = getLoanById(id);

        if (loan.getStatus() != LoanStatus.PENDING) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Only pending loans can be cancelled");
        }

        loan.setStatus(LoanStatus.CANCELLED);

        return loanRepository.save(loan);
    }

    public List<Loan> getLoansByCustomerId(Long customerId) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found");
        }

        return loanRepository.findByCustomer_Id(customerId);
    }

    public Page<Loan> searchLoans(
            String loanType,
            LoanStatus status,
            int page,
            int size,
            String sortBy,
            String direction) {

        if (page < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page number cannot be negative");
        }

        if (size < 1 || size > 100) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Page size must be between 1 and 100");
        }

        List<String> allowedSortFields = List.of("id", "loanType", "amount", "tenure", "status");

        if (!allowedSortFields.contains(sortBy)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid sort field");
        }

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Direction must be asc or desc");
        }

        if (loanType != null && loanType.isBlank()) {
            loanType = null;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy));

        return loanRepository.searchLoans(
                loanType,
                status,
                pageable);
    }

    public LoanDashboardSummary getDashboardSummary() {

        long totalLoans = loanRepository.count();

        long pendingLoans = loanRepository.countByStatus(LoanStatus.PENDING);

        long approvedLoans = loanRepository.countByStatus(LoanStatus.APPROVED) + loanRepository.countByStatus(LoanStatus.ACTIVE);

        long rejectedLoans = loanRepository.countByStatus(LoanStatus.REJECTED);

        double totalLoanAmount = loanRepository.getTotalLoanAmount();

        double totalApprovedLoanAmount = loanRepository.getTotalAmountByStatus(
                LoanStatus.APPROVED) + loanRepository.getTotalAmountByStatus(LoanStatus.ACTIVE);

        return new LoanDashboardSummary(
                totalLoans,
                pendingLoans,
                approvedLoans,
                rejectedLoans,
                totalLoanAmount,
                totalApprovedLoanAmount);
    }

    public LoanEmiResponse calculateEmi(
            Long loanId,
            double annualInterestRate) {

        Loan loan = getLoanById(loanId);

        if (annualInterestRate < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Interest rate cannot be negative");
        }

        double principal = loan.getAmount();
        int tenureMonths = loan.getTenure();

        if (principal <= 0 || tenureMonths <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Loan amount and tenure must be greater than zero");
        }

        double monthlyRate = annualInterestRate / 12 / 100;

        double monthlyEmi;

        if (monthlyRate == 0) {
            monthlyEmi = principal / tenureMonths;
        } else {
            monthlyEmi = principal
                    * monthlyRate
                    * Math.pow(1 + monthlyRate, tenureMonths)
                    / (Math.pow(1 + monthlyRate, tenureMonths) - 1);
        }

        monthlyEmi = round(monthlyEmi);

        double totalPayableAmount = round(monthlyEmi * tenureMonths);

        double totalInterest = round(totalPayableAmount - principal);

        List<RepaymentScheduleItem> schedule = new ArrayList<>();

        double remainingBalance = principal;

        for (int month = 1; month <= tenureMonths; month++) {

            double interestPaid = round(remainingBalance * monthlyRate);

            double principalPaid = round(monthlyEmi - interestPaid);

            if (month == tenureMonths) {
                principalPaid = round(remainingBalance);

                monthlyEmi = round(principalPaid + interestPaid);
            }

            remainingBalance = round(remainingBalance - principalPaid);

            if (remainingBalance < 0) {
                remainingBalance = 0;
            }

            schedule.add(
                    new RepaymentScheduleItem(
                            month,
                            monthlyEmi,
                            principalPaid,
                            interestPaid,
                            remainingBalance));
        }

        return new LoanEmiResponse(
                loanId,
                principal,
                tenureMonths,
                annualInterestRate,
                monthlyEmi,
                totalPayableAmount,
                totalInterest,
                schedule);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}