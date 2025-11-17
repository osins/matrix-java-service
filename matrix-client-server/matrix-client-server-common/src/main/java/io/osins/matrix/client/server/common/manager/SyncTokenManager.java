package io.osins.matrix.client.server.common.manager;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class SyncTokenManager {

    private final AtomicLong counter = new AtomicLong();
    private final ConcurrentHashMap<Long, String> userLastToken = new ConcurrentHashMap<>();

    public String generateToken(Long userId) {
        String token = "s" + counter.incrementAndGet();
        userLastToken.put(userId, token);
        return token;
    }

    public String getLastToken(Long userId) {
        return userLastToken.get(userId);
    }
}