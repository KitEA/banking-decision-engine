package com.kit.banking_decision_engine.dto;

public record DecisionResult (
        boolean approved,
        int approvedAmount,
        int approvedPeriod
) {}
