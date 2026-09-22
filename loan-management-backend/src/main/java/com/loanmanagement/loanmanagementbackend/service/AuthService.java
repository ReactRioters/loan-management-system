package com.loanmanagement.loanmanagementbackend.service;

import com.loanmanagement.loanmanagementbackend.model.Customer;
import com.loanmanagement.loanmanagementbackend.model.JwtResponse;
import com.loanmanagement.loanmanagementbackend.model.LoginRequest;
import com.loanmanagement.loanmanagementbackend.model.RegisterRequest;
import com.loanmanagement.loanmanagementbackend.model.Role;
import com.loanmanagement.loanmanagementbackend.model.User;
import com.loanmanagement.loanmanagementbackend.repository.CustomerRepository;
import com.loanmanagement.loanmanagementbackend.repository.UserRepository;
import com.loanmanagement.loanmanagementbackend.security.JwtUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(
            UserRepository userRepository,
            CustomerRepository customerRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public User registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use!");
        }

        Role role = request.getRole() != null ? request.getRole() : Role.ROLE_CUSTOMER;

        Customer customer = null;
        if (role == Role.ROLE_CUSTOMER) {
            String name = (request.getName() != null && !request.getName().isBlank())
                    ? request.getName()
                    : request.getUsername();
            Customer newCustomer = new Customer();
            newCustomer.setName(name);
            newCustomer.setEmail(request.getEmail());
            customer = customerRepository.save(newCustomer);
        }

        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail(),
                role,
                customer
        );

        return userRepository.save(user);
    }

    public JwtResponse authenticateUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        String jwt = jwtUtils.generateToken(user.getUsername(), user.getRole().name());

        Long customerId = user.getCustomer() != null ? user.getCustomer().getId() : null;

        return new JwtResponse(
                jwt,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                customerId
        );
    }
}
