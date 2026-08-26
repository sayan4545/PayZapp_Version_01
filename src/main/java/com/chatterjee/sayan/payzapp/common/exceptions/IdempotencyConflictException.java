package com.chatterjee.sayan.payzapp.common.exceptions;

public class IdempotencyConflictException extends RuntimeException{
    public IdempotencyConflictException(String message) {
        super(message);
    }
}
