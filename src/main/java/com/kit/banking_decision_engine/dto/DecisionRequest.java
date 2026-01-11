package com.kit.banking_decision_engine.dto;

public record DecisionRequest (
        String personalCode,
        int loanAmount,
        int loanPeriod
) {}
