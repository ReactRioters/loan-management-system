package com.loanmanagement.loanmanagementbackend.repository;

import com.loanmanagement.loanmanagementbackend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("""
        SELECT COUNT(DISTINCT c.id)
        FROM Customer c
        JOIN Loan l ON l.customer.id = c.id
        """)
    long countCustomersWithLoans();

    @Query("""
        SELECT COUNT(c)
        FROM Customer c
        WHERE c.id NOT IN (
            SELECT DISTINCT l.customer.id
            FROM Loan l
        )
        """)
    long countCustomersWithoutLoans();
}