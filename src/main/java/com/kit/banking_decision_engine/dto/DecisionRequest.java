package com.kit.banking_decision_engine.dto;

import jakarta.validation.constraints.*;

public record DecisionRequest (
        @NotBlank(message = "Personal code must not be blank")
        @Pattern(
                regexp = "\\d{11}",
                message = "Personal code must be exactly 11 digits"
        )
        String personalCode,

        @NotNull(message = "Loan amount is required")
        @Min(value = 2000, message = "Loan amount must be at least 2000")
        @Max(value = 10000, message = "Loan amount must not exceed 10000")
        int loanAmount,

        @NotNull(message = "Loan period is required")
        @Min(value = 12, message = "Loan period must be at least 12 months")
        @Max(value = 60, message = "Loan period must not exceed 60 months")
        int loanPeriod
) {}
