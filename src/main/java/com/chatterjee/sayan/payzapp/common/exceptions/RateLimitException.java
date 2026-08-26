package com.chatterjee.sayan.payzapp.common.exceptions;

import lombok.Getter;
import org.aspectj.apache.bcel.classfile.annotation.RuntimeInvisTypeAnnos;

@Getter
public class RateLimitException extends RuntimeException {
    private final int retryAfterSeconds;
    private final int remaining;

    public RateLimitException(String message, int retryAfterSeconds) {
        super(message);
        this.retryAfterSeconds = retryAfterSeconds;
        this.remaining = 0;
    }
}
