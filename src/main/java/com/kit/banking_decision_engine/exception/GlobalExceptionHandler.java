package com.kit.banking_decision_engine.exception;

import com.kit.banking_decision_engine.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(
            MethodArgumentNotValidException ex
    ) {
        String firstError = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        log.warn("Validation failed: {}", firstError);

        return new ErrorResponse(
                "VALIDATION_ERROR",
                firstError
        );
    }

    @ExceptionHandler(UnknownValueException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUnknownArgument(
            UnknownValueException ex
    ) {
        log.warn("Business rule violation: {}", ex.getMessage());

        return new ErrorResponse(
                "UNKNOWN_VALUE",
                ex.getMessage()
        );
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedException(Exception ex) {
        log.error("Unhandled exception occurred: ", ex);

        return new ErrorResponse(
                "INTERNAL_SERVER_ERROR",
                "An unexpected error occurred. Please try again later."
        );
    }
}