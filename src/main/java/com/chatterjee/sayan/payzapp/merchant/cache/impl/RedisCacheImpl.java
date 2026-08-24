package com.chatterjee.sayan.payzapp.merchant.cache.impl;

import com.chatterjee.sayan.payzapp.common.config.RedisConfig;
import com.chatterjee.sayan.payzapp.merchant.cache.ApiKeyCache;
import com.chatterjee.sayan.payzapp.merchant.cache.ApiKeyCacheEntry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisCacheImpl implements ApiKeyCache {

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    private static final String PREFIX = "apiKey:";
    private static final Duration TTL = Duration.ofMinutes(5);
    @Override
    public Optional<ApiKeyCacheEntry> get(String keyId) {

        try{
            String json = stringRedisTemplate.opsForValue().get(PREFIX + keyId);
            if(json == null){
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(json, ApiKeyCacheEntry.class));
        }catch (Exception e){
            log.warn("Api key cache hit failed , keyId : {}",keyId);
            return Optional.empty();
        }
    }

    @Override
    public void put(String keyId, ApiKeyCacheEntry entry) {

        try{
            stringRedisTemplate.opsForValue().set(PREFIX + keyId, objectMapper.writeValueAsString(entry),TTL);
        }catch (Exception e){
            log.warn("Api key cache put failed, keyId : {}",keyId);
        }

    }

    @Override
    public void evict(String keyId) {
        stringRedisTemplate.delete(PREFIX + keyId);

    }
}
