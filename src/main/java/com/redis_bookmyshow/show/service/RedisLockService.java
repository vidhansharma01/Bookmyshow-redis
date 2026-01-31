package com.redis_bookmyshow.show.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;

@Service
public class RedisLockService {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisLockService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Try to acquire lock
     */
    public boolean acquireLock(String key, String value, long ttlSeconds) {
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, value, Duration.ofSeconds(ttlSeconds));

        return Boolean.TRUE.equals(success);
    }

    /**
     * Safely release lock using Lua script
     */
    public void releaseLock(String key, String value) {
        String script =
                "if redis.call('GET', KEYS[1]) == ARGV[1] then " +
                        "return redis.call('DEL', KEYS[1]) " +
                        "else return 0 end";

        redisTemplate.execute(
                new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(key),
                value
        );
    }
}
