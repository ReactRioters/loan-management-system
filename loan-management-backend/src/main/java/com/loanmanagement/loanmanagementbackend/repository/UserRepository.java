package com.loanmanagement.loanmanagementbackend.repository;

import com.loanmanagement.loanmanagementbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByCustomer_Id(Long customerId);
}
