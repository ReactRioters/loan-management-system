package com.loanmanagement.loanmanagementbackend.repository;

import com.loanmanagement.loanmanagementbackend.model.Repayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepaymentRepository
        extends JpaRepository<Repayment, Long> {

    List<Repayment> findByLoan_IdOrderByPaymentDateDesc(Long loanId);

    @Query("""
        SELECT COALESCE(SUM(r.amount), 0)
        FROM Repayment r
        WHERE r.loan.id = :loanId
        """)
    double getTotalPaidByLoanId(@Param("loanId") Long loanId);
}