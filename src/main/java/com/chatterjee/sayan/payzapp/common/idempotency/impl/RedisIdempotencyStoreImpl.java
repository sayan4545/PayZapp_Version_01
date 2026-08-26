package com.chatterjee.sayan.payzapp.common.idempotency.impl;

import com.chatterjee.sayan.payzapp.common.idempotency.IdempotencyStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisIdempotencyStoreImpl implements IdempotencyStore {
    private final StringRedisTemplate redis;
    private static final String PREFIX = "idempotency:";
    @Override
    public boolean setIfAbsent(String key, Duration ttl) {
        try{
            Boolean set = redis.opsForValue().setIfAbsent(PREFIX+key,IN_PROGRESS,ttl);
            return Boolean.TRUE.equals(set);
        }catch (DataAccessException d){
            log.warn("Idempotency store is not working, failing open for key : {}",key);
            return true;

        }
    }

    @Override
    public void store(String key, String value, Duration ttl) {
        try{
            redis.opsForValue().set(PREFIX+key,value,ttl);

        }catch (DataAccessException d){
            log.warn("Failed to presist, failing open for the key : {}",key,d);
        }
    }

    @Override
    public Optional<String> get(String key) {
        try {
            return Optional.ofNullable(redis.opsForValue().get(PREFIX + key));
        }catch (DataAccessException d){
            log.warn("Failed to fetch, failing open for the key : {}",key,d);
            return Optional.empty();
        }
    }

    @Override
    public void delete(String key) {
        try{
            redis.delete(PREFIX + key);
        }catch (DataAccessException d){
            log.warn("Failed to delete, failing open for the key : {}",key,d);
        }
    }
}
