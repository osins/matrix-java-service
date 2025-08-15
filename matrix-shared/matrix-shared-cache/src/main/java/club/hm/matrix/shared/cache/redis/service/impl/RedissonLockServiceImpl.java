package club.hm.matrix.shared.cache.redis.service.impl;

import club.hm.matrix.shared.cache.redis.exp.RedisRuntimeException;
import club.hm.matrix.shared.cache.redis.exp.RedisLockException;
import club.hm.matrix.shared.cache.redis.service.IRedissonLockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedissonLockServiceImpl implements IRedissonLockService {
    private final RedissonClient redissonClient;

    @Override
    public void executeWithLock(String lockKey, Runnable runnable, long waitTime, long leaseTime) {
        var lock = redissonClient.getLock(lockKey);
        var locked = false;

        try {
            locked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

            if (locked) {
                // 获取锁成功，执行方法
                runnable.run();
            } else {
                throw new RedisLockException("获取分布式锁失败，lockKey: " + lockKey);
            }
        } catch (Exception ex) {
            log.error("分布式锁异常: {}, {}", lockKey, ex.getMessage(), ex);
        } finally {
            // 如果获取锁成功，释放锁
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public <T> T executeWithLock(String lockKey, Supplier<T> supplier, long waitTime, long leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        boolean locked = false;

        try {
            locked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);

            if (locked) {
                // 获取锁成功，执行方法并返回结果
                return supplier.get();
            } else {
                throw new RedisRuntimeException("获取分布式锁失败，lockKey: " + lockKey);
            }
        } catch (Exception ex) {
            log.error("分布式锁异常: {}, {}", lockKey, ex.getMessage(), ex);
            return null;
        } finally {
            // 如果获取锁成功，释放锁
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public void executeWithLock(String lockKey, Runnable runnable) {
        executeWithLock(lockKey, runnable, 10, 30);
    }

    @Override
    public <T> T executeWithLock(String lockKey, Supplier<T> supplier) {
        return executeWithLock(lockKey, supplier, 10, 30);
    }
}
