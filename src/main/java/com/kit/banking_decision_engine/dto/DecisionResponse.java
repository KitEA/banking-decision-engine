package com.kit.banking_decision_engine.dto;

public record DecisionResponse (
        boolean approved,
        int amount
) {}
