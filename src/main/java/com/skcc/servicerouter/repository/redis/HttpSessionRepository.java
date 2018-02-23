package com.skcc.servicerouter.repository.redis;

import com.skcc.servicerouter.repository.HttpSession;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class HttpSessionRepository {
    private HashOperations<String, String, Object> hashOps;

    private static final String KEY_PREFIX = "spring:session:sessions:";

    public HttpSessionRepository(RedisTemplate redisTemplate) {
        this.hashOps = redisTemplate.opsForHash();
    }

    public HttpSession getHttpSession(String jsessionId) {
        String sessionId = KEY_PREFIX + ":" + jsessionId;
        long creationTime = (long)hashOps.get(sessionId, "creationTime");
        int maxInactiveInterval = (int)hashOps.get(sessionId, "maxInactiveInterval");
        long lastAccessedTime = (long)hashOps.get(sessionId, "lastAccessedTime");

        return new HttpSession(creationTime, maxInactiveInterval, lastAccessedTime);
    }
}
