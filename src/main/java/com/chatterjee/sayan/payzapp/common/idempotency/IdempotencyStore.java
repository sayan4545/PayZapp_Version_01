package com.chatterjee.sayan.payzapp.common.idempotency;

import java.time.Duration;
import java.util.Optional;

public interface IdempotencyStore {

    String IN_PROGRESS = "__InProgress__";

    boolean setIfAbsent(String key , Duration ttl);
    void store(String key,String value, Duration ttl);
    Optional<String> get(String key);
    void delete(String key);
}
