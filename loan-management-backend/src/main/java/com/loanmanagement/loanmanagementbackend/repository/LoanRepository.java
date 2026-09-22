
package com.loanmanagement.loanmanagementbackend.repository;

import com.loanmanagement.loanmanagementbackend.model.Loan;
import com.loanmanagement.loanmanagementbackend.model.LoanStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByCustomer_Id(Long customerId);

    @Query("""
            SELECT l FROM Loan l
            WHERE (:loanType IS NULL OR
                   LOWER(l.loanType) LIKE LOWER(CONCAT('%', :loanType, '%')))
            AND (:status IS NULL OR l.status = :status)
            """)
    Page<Loan> searchLoans(
            @Param("loanType") String loanType,
            @Param("status") LoanStatus status,
            Pageable pageable);

    long countByStatus(LoanStatus status);

    @Query("""
            SELECT COALESCE(SUM(l.amount), 0)
            FROM Loan l
            """)
    double getTotalLoanAmount();

    @Query("""
            SELECT COALESCE(SUM(l.amount), 0)
            FROM Loan l
            WHERE l.status = :status
            """)
    double getTotalAmountByStatus(@Param("status") LoanStatus status);
}