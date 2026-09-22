package com.loanmanagement.loanmanagementbackend.controller;

import com.loanmanagement.loanmanagementbackend.model.Repayment;
import com.loanmanagement.loanmanagementbackend.model.CreateRepaymentRequest;
import com.loanmanagement.loanmanagementbackend.model.RepaymentSummary;
import com.loanmanagement.loanmanagementbackend.service.RepaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repayments")
public class RepaymentController {

    private final RepaymentService repaymentService;

    public RepaymentController(
            RepaymentService repaymentService) {

        this.repaymentService = repaymentService;
    }

    @PostMapping
@ResponseStatus(HttpStatus.CREATED)
public Repayment createRepayment(
        @Valid @RequestBody CreateRepaymentRequest request) {

    return repaymentService.createRepayment(request);
}

    @GetMapping("/loan/{loanId}")
    public List<Repayment> getRepaymentsByLoanId(
            @PathVariable Long loanId) {

        return repaymentService
                .getRepaymentsByLoanId(loanId);
    }

    @GetMapping("/loan/{loanId}/summary")
    public RepaymentSummary getRepaymentSummary(
            @PathVariable Long loanId) {

        return repaymentService
                .getRepaymentSummary(loanId);
    }
}