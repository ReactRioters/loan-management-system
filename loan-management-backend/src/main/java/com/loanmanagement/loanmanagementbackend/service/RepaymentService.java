package com.loanmanagement.loanmanagementbackend.service;

import com.loanmanagement.loanmanagementbackend.model.CreateRepaymentRequest;
import com.loanmanagement.loanmanagementbackend.model.Loan;
import com.loanmanagement.loanmanagementbackend.model.Repayment;
import com.loanmanagement.loanmanagementbackend.model.RepaymentStatus;
import com.loanmanagement.loanmanagementbackend.model.RepaymentSummary;
import com.loanmanagement.loanmanagementbackend.repository.LoanRepository;
import com.loanmanagement.loanmanagementbackend.repository.RepaymentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class RepaymentService {

        private final RepaymentRepository repaymentRepository;
        private final LoanRepository loanRepository;

        public RepaymentService(
                        RepaymentRepository repaymentRepository,
                        LoanRepository loanRepository) {

                this.repaymentRepository = repaymentRepository;
                this.loanRepository = loanRepository;
        }

        public Repayment createRepayment(
                        CreateRepaymentRequest request) {

                Loan loan = loanRepository.findById(request.getLoanId())
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Loan not found"));

                if (loan.getStatus() != com.loanmanagement.loanmanagementbackend.model.LoanStatus.APPROVED
                                && loan.getStatus() != com.loanmanagement.loanmanagementbackend.model.LoanStatus.ACTIVE) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Repayments can only be processed for APPROVED or ACTIVE loans");
                }

                double amount = request.getAmount();

                double totalPaid = repaymentRepository.getTotalPaidByLoanId(loan.getId());

                double remainingAmount = loan.getAmount() - totalPaid;

                if (amount > remainingAmount) {
                        throw new ResponseStatusException(
                                        HttpStatus.BAD_REQUEST,
                                        "Repayment amount cannot exceed remaining loan amount");
                }

                Repayment repayment = new Repayment();

                repayment.setAmount(amount);

                repayment.setPaymentDate(
                                request.getPaymentDate() != null
                                                ? request.getPaymentDate()
                                                : LocalDate.now());

                repayment.setLoan(loan);
                repayment.setStatus(RepaymentStatus.PAID);

                Repayment savedRepayment = repaymentRepository.save(repayment);

                double newTotalPaid = totalPaid + amount;
                if (newTotalPaid >= loan.getAmount()) {
                        loan.setStatus(com.loanmanagement.loanmanagementbackend.model.LoanStatus.CLOSED);
                        loanRepository.save(loan);
                } else if (loan.getStatus() == com.loanmanagement.loanmanagementbackend.model.LoanStatus.APPROVED) {
                        loan.setStatus(com.loanmanagement.loanmanagementbackend.model.LoanStatus.ACTIVE);
                        loanRepository.save(loan);
                }

                return savedRepayment;
        }

        public List<Repayment> getRepaymentsByLoanId(Long loanId) {

                loanRepository.findById(loanId)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Loan not found"));

                return repaymentRepository
                                .findByLoan_IdOrderByPaymentDateDesc(loanId);
        }

        public RepaymentSummary getRepaymentSummary(Long loanId) {

                Loan loan = loanRepository.findById(loanId)
                                .orElseThrow(() -> new ResponseStatusException(
                                                HttpStatus.NOT_FOUND,
                                                "Loan not found"));

                double totalPaid = repaymentRepository.getTotalPaidByLoanId(loanId);

                double remainingAmount = Math.max(loan.getAmount() - totalPaid, 0);

                long totalPayments = repaymentRepository
                                .findByLoan_IdOrderByPaymentDateDesc(loanId)
                                .size();

                return new RepaymentSummary(
                                loanId,
                                loan.getAmount(),
                                totalPaid,
                                remainingAmount,
                                totalPayments);
        }
}