package io.osins.matrix.shared.cache.redis.exp;

public class RedisRuntimeException extends RuntimeException{
    public RedisRuntimeException(String message) {
        super(message);
    }
}
