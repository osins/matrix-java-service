package io.osins.matrix.shared.cache.redis.service;

import java.util.function.Supplier;

public interface IRedissonLockService {
    /**
     * 在分布式锁保护下执行无返回值的方法
     *
     * @param lockKey    锁的key
     * @param runnable   需要执行的方法
     * @param waitTime   获取锁最大等待时间(秒)
     * @param leaseTime  持有锁的时间(秒)，超过这个时间自动释放锁
     * @throws InterruptedException 如果获取锁的过程被中断
     * @throws RuntimeException 如果获取锁失败
     */
    void executeWithLock(String lockKey, Runnable runnable, long waitTime, long leaseTime);

    /**
     * 在分布式锁保护下执行有返回值的方法
     *
     * @param lockKey    锁的key
     * @param supplier   需要执行的方法
     * @param waitTime   获取锁最大等待时间(秒)
     * @param leaseTime  持有锁的时间(秒)，超过这个时间自动释放锁
     * @param <T>        返回值类型
     * @return           方法执行结果
     * @throws InterruptedException 如果获取锁的过程被中断
     * @throws RuntimeException 如果获取锁失败
     */
    <T> T executeWithLock(String lockKey, Supplier<T> supplier, long waitTime, long leaseTime);

    /**
     * 使用默认等待和租期时间在分布式锁保护下执行无返回值的方法
     * 默认等待10秒，持有锁30秒
     *
     * @param lockKey  锁的key
     * @param runnable 需要执行的方法
     * @throws InterruptedException 如果获取锁的过程被中断
     */
    void executeWithLock(String lockKey, Runnable runnable);

    /**
     * 使用默认等待和租期时间在分布式锁保护下执行有返回值的方法
     * 默认等待10秒，持有锁30秒
     *
     * @param lockKey  锁的key
     * @param supplier 需要执行的方法
     * @param <T>      返回值类型
     * @return         方法执行结果
     * @throws InterruptedException 如果获取锁的过程被中断
     */
    <T> T executeWithLock(String lockKey, Supplier<T> supplier);
}
