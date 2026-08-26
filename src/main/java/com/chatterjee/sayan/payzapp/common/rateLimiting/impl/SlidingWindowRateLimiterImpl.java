package com.chatterjee.sayan.payzapp.common.rateLimiting.impl;

import com.chatterjee.sayan.payzapp.common.rateLimiting.RateLimitResult;
import com.chatterjee.sayan.payzapp.common.rateLimiting.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "app.rate-limit.method",havingValue = "sliding")
public class SlidingWindowRateLimiterImpl implements RateLimiter {
    private final StringRedisTemplate redis;
    @Override
    public RateLimitResult check(String key, int maxRequestAllowed, long windowSeconds) {
        long nowMs = System.currentTimeMillis();
        long floorMs = nowMs - windowSeconds*1000;

        String redisKey = "ratelimit:sliding:"+ key;
        var Zset = redis.opsForZSet();
        Zset.removeRangeByScore(redisKey,Double.NEGATIVE_INFINITY,floorMs);

        Long count = Zset.zCard(redisKey);
        long current = count!=null?count:0;
        if(current>= maxRequestAllowed){

            var oldest = Zset.rangeWithScores(redisKey,0,0);
            int retryAfter = 1;

            if((oldest!=null && !oldest.isEmpty())){
                Double oldestScore = oldest.iterator().next().getScore();
                if(oldestScore!=null){
                    long windowExpiresMs = oldestScore.longValue()+windowSeconds*1000;
                    retryAfter = (int) Math.ceil((windowExpiresMs-nowMs)/1000.0);

                }
            }
            return RateLimitResult.denied(retryAfter);
        }

        Zset.add(redisKey, UUID.randomUUID().toString(),nowMs);
        redis.expire(redisKey, Duration.ofSeconds(windowSeconds+1));
        return RateLimitResult.allowed((int) (maxRequestAllowed-current -1));

    }
}
