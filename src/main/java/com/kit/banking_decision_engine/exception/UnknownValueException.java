package com.kit.banking_decision_engine.exception;

public class UnknownValueException extends RuntimeException {
    public UnknownValueException(String errorMessage) {
        super(errorMessage);
    }
}
