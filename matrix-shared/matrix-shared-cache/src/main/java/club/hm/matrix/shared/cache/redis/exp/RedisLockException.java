package club.hm.matrix.shared.cache.redis.exp;

public class RedisLockException extends RuntimeException{
    public RedisLockException(String message) {
        super(message);
    }

    public RedisLockException(String message, Throwable cause) {
        super(message, cause);
    }
}
