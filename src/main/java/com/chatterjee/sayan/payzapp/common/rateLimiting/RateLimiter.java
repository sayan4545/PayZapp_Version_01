package com.chatterjee.sayan.payzapp.common.rateLimiting;

public interface RateLimiter {

    RateLimitResult check(String key, int maxRequestAllowed,long windowSeconds);
}
