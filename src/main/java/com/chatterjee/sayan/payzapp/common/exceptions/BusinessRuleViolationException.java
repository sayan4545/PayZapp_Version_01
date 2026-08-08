package com.chatterjee.sayan.payzapp.common.exceptions;

import lombok.Getter;

@Getter
public class BusinessRuleViolationException extends RuntimeException{

    private final String errorCode;

    public BusinessRuleViolationException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
