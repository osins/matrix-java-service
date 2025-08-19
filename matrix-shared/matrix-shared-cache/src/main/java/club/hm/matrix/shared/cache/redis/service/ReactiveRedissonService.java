package club.hm.matrix.shared.cache.redis.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucketReactive;
import org.redisson.api.RListReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Redisson响应式封装服务类
 * 提供常用的Redis操作方法，支持响应式编程
 *
 * @author Your Name
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public final class ReactiveRedissonService {

    private final RedissonReactiveClient redissonReactiveClient;

    // ================================ String 操作 ================================

    /**
     * 设置键值对
     */
    public <T> Mono<Void> set(String key, T value) {
        return getBucket(key).set(value)
                .doOnSuccess(v -> log.debug("Set key: {}", key))
                .doOnError(e -> log.error("Failed to set key: {}", key, e));
    }

    /**
     * 设置键值对并指定过期时间
     */
    public <T> Mono<Void> setWithExpire(String key, T value, Duration timeout) {
        return getBucket(key).set(value, timeout)
                .doOnSuccess(result -> log.debug("Set key: {} with expire: {}", key, timeout))
                .doOnError(e -> log.error("Failed to set key with expire: {}", key, e));
    }

    /**
     * 获取值
     */
    public <T> Mono<T> get(String key, Class<T> type) {
        return getBucket(key).get()
                .cast(type)
                .doOnSuccess(v -> log.debug("Get key: {}, value: {}", key, v))
                .doOnError(e -> log.error("Failed to get key: {}", key, e));
    }

    /**
     * 获取并删除
     */
    public <T> Mono<T> getAndDelete(String key, Class<T> type) {
        return getBucket(key).getAndDelete()
                .cast(type)
                .doOnSuccess(v -> log.debug("Get and delete key: {}", key))
                .doOnError(e -> log.error("Failed to get and delete key: {}", key, e));
    }

    /**
     * 检查键是否存在
     */
    public Mono<Boolean> exists(String key) {
        return getBucket(key).isExists()
                .doOnSuccess(exists -> log.debug("Key exists: {}, result: {}", key, exists))
                .doOnError(e -> log.error("Failed to check key exists: {}", key, e));
    }

    /**
     * 删除键
     */
    public Mono<Boolean> delete(String key) {
        return getBucket(key).delete()
                .doOnSuccess(deleted -> log.debug("Delete key: {}, result: {}", key, deleted))
                .doOnError(e -> log.error("Failed to delete key: {}", key, e));
    }

    /**
     * 设置过期时间
     */
    public Mono<Boolean> expire(String key, Duration timeout) {
        return getBucket(key).expire(timeout)
                .doOnSuccess(result -> log.debug("Set expire for key: {}, timeout: {}", key, timeout))
                .doOnError(e -> log.error("Failed to set expire for key: {}", key, e));
    }

    // ================================ List 操作 ================================

    /**
     * 向列表右侧添加元素
     */
    public <T> Mono<Boolean> listRightPush(String key, T value) {
        return getList(key).add(value)
                .doOnSuccess(size -> log.debug("List right push key: {}, new size: {}", key, size))
                .doOnError(e -> log.error("Failed to list right push key: {}", key, e));
    }

    /**
     * 向列表右侧添加多个元素
     */
    public <T> Mono<Boolean> listRightPushAll(String key, Collection<T> values) {
        return getList(key).addAll(values)
                .doOnSuccess(result -> log.debug("List right push all key: {}, count: {}", key, values.size()))
                .doOnError(e -> log.error("Failed to list right push all key: {}", key, e));
    }

    /**
     * 从列表左侧弹出元素
     */
    public <T> Mono<T> listLeftPop(String key, Class<T> type) {
        return getList(key).fastRemove(0)
                .cast(type)
                .doOnSuccess(v -> log.debug("List left pop key: {}", key))
                .doOnError(e -> log.error("Failed to list left pop key: {}", key, e));
    }

    /**
     * 获取列表指定范围的元素
     */
    public <T> Flux<T> listRange(String key, int fromIndex, int toIndex, Class<T> type) {
        return getList(key).range(fromIndex, toIndex)
                .flatMapMany(Flux::fromIterable)           // 转成 Flux<V>
                .cast(type)
                .doOnNext(r -> log.debug("List range key: {}, from: {}, to: {}, result: {}", key, fromIndex, toIndex, r))
                .doOnError(e -> log.error("Failed to list range key: {}", key, e));
    }

    /**
     * 获取列表大小
     */
    public Mono<Integer> listSize(String key) {
        return getList(key).size()
                .doOnSuccess(size -> log.debug("List size key: {}, size: {}", key, size))
                .doOnError(e -> log.error("Failed to get list size key: {}", key, e));
    }

    // ================================ Set 操作 ================================

    /**
     * 向集合添加元素
     */
    public <T> Mono<Boolean> setAdd(String key, T value) {
        return getSet(key).add(value)
                .doOnSuccess(added -> log.debug("Set add key: {}, added: {}", key, added))
                .doOnError(e -> log.error("Failed to set add key: {}", key, e));
    }

    /**
     * 向集合添加多个元素
     */
    public <T> Mono<Boolean> setAddAll(String key, Collection<T> values) {
        return getSet(key).addAll(values)
                .doOnSuccess(added -> log.debug("Set add all key: {}, count: {}", key, values.size()))
                .doOnError(e -> log.error("Failed to set add all key: {}", key, e));
    }

    /**
     * 从集合移除元素
     */
    public <T> Mono<Boolean> setRemove(String key, T value) {
        return getSet(key).remove(value)
                .doOnSuccess(removed -> log.debug("Set remove key: {}, removed: {}", key, removed))
                .doOnError(e -> log.error("Failed to set remove key: {}", key, e));
    }

    /**
     * 检查集合是否包含元素
     */
    public <T> Mono<Boolean> setContains(String key, T value) {
        return getSet(key).contains(value)
                .doOnSuccess(contains -> log.debug("Set contains key: {}, contains: {}", key, contains))
                .doOnError(e -> log.error("Failed to check set contains key: {}", key, e));
    }

    /**
     * 获取集合所有元素
     */
    public <T> Flux<T> setMembers(String key, Class<T> type) {
        return getSet(key).iterator()
                .cast(type)
                .doOnComplete(() -> log.debug("Set members key: {}", key))
                .doOnError(e -> log.error("Failed to get set members key: {}", key, e));
    }

    // ================================ Hash 操作 ================================

    /**
     * 设置哈希字段值
     */
    public <K, V> Mono<V> hashPut(String key, K field, V value, Class<V> type) {
        return getMap(key).put(field, value)
                .map(type::cast)
                .doOnSuccess(v -> log.debug("Hash put key: {}, field: {}", key, field))
                .doOnError(e -> log.error("Failed to hash put key: {}, field: {}", key, field, e));
    }

    /**
     * 获取哈希字段值
     */
    public <K, V> Mono<V> hashGet(String key, K field, Class<V> valueType) {
        return getMap(key).get(field)
                .cast(valueType)
                .doOnSuccess(v -> log.debug("Hash get key: {}, field: {}", key, field))
                .doOnError(e -> log.error("Failed to hash get key: {}, field: {}", key, field, e));
    }

    /**
     * 删除哈希字段
     */
    @SafeVarargs
    public final <K> Mono<Long> hashDelete(String key, K... fields) {
        return getMap(key).fastRemove(fields)
                .doOnSuccess(count -> log.debug("Hash delete key: {}, removed count: {}", key, count))
                .doOnError(e -> log.error("Failed to hash delete key: {}", key, e));
    }

    /**
     * 检查哈希字段是否存在
     */
    public <K> Mono<Boolean> hashExists(String key, K field) {
        return getMap(key).containsKey(field)
                .doOnSuccess(exists -> log.debug("Hash exists key: {}, field: {}, exists: {}", key, field, exists))
                .doOnError(e -> log.error("Failed to check hash exists key: {}, field: {}", key, field, e));
    }

    /**
     * 获取哈希所有字段值
     */
    public <K, V> Mono<Map<K, V>> hashGetAll(String key, Class<K> keyType, Class<V> valueType) {
        return getMap(key).readAllMap()
                .map(m -> m.entrySet().stream()
                        .collect(Collectors.toMap(
                                e -> keyType.cast(e.getKey()),
                                e -> valueType.cast(e.getValue())
                        )))
                .doOnSuccess(map -> log.debug("Hash get all key: {}, size: {}", key, map.size()))
                .doOnError(e -> log.error("Failed to hash get all key: {}", key, e));
    }

    /**
     * 批量设置哈希字段
     */
    public <K, V> Mono<Void> hashPutAll(String key, Map<K, V> map) {
        return getMap(key).putAll(map)
                .doOnSuccess(v -> log.debug("Hash put all key: {}, count: {}", key, map.size()))
                .doOnError(e -> log.error("Failed to hash put all key: {}", key, e));
    }

    // ================================ 分布式锁操作 ================================

    /**
     * 尝试获取锁
     */
    public Mono<Boolean> tryLock(String lockKey, long leaseTime, TimeUnit timeUnit) {
        return redissonReactiveClient.getLock(lockKey)
                .tryLock(leaseTime, timeUnit)
                .doOnSuccess(acquired -> log.debug("Try lock key: {}, acquired: {}", lockKey, acquired))
                .doOnError(e -> log.error("Failed to try lock key: {}", lockKey, e));
    }

    /**
     * 释放锁
     */
    public Mono<Void> unlock(String lockKey) {
        return redissonReactiveClient.getLock(lockKey)
                .unlock()
                .doOnSuccess(v -> log.debug("Unlock key: {}", lockKey))
                .doOnError(e -> log.error("Failed to unlock key: {}", lockKey, e));
    }

    /**
     * 执行带锁的操作
     */
    public <T> Mono<T> executeWithLock(String lockKey, long leaseTime, TimeUnit timeUnit, Mono<T> operation) {
        return tryLock(lockKey, leaseTime, timeUnit)
                .flatMap(acquired -> {
                    if (acquired) {
                        return operation
                                .doOnSuccess(result-> log.debug("Operation result[lock:{}]: {}", lockKey, result))
                                .flatMap(result -> unlock(lockKey).thenReturn(result));
                    } else {
                        return Mono.error(new RuntimeException("Failed to acquire lock: " + lockKey));
                    }
                });
    }

    // ================================ 发布订阅操作 ================================

    /**
     * 发布消息
     */
    public <T> Mono<Long> publish(String channel, T message) {
        return redissonReactiveClient.getTopic(channel)
                .publish(message)
                .doOnSuccess(count -> log.debug("Published message to channel: {}, receivers: {}", channel, count))
                .doOnError(e -> log.error("Failed to publish message to channel: {}", channel, e));
    }

    /**
     * 订阅消息
     */
    public <T> Flux<T> subscribe(String channel, Class<T> messageType) {
        return redissonReactiveClient.getTopic(channel)
                .getMessages(messageType)
                .doOnSubscribe(s -> log.debug("Subscribed to channel: {}", channel))
                .doOnError(e -> log.error("Failed to subscribe to channel: {}", channel, e));
    }

    // ================================ 计数器操作 ================================

    /**
     * 递增计数器
     */
    public Mono<Long> increment(String key) {
        return redissonReactiveClient.getAtomicLong(key)
                .incrementAndGet()
                .doOnSuccess(value -> log.debug("Increment key: {}, value: {}", key, value))
                .doOnError(e -> log.error("Failed to increment key: {}", key, e));
    }

    /**
     * 递增计数器指定值
     */
    public Mono<Long> incrementBy(String key, long delta) {
        return redissonReactiveClient.getAtomicLong(key)
                .addAndGet(delta)
                .doOnSuccess(value -> log.debug("Increment by key: {}, delta: {}, value: {}", key, delta, value))
                .doOnError(e -> log.error("Failed to increment by key: {}", key, e));
    }

    /**
     * 递减计数器
     */
    public Mono<Long> decrement(String key) {
        return redissonReactiveClient.getAtomicLong(key)
                .decrementAndGet()
                .doOnSuccess(value -> log.debug("Decrement key: {}, value: {}", key, value))
                .doOnError(e -> log.error("Failed to decrement key: {}", key, e));
    }

    /**
     * 获取计数器值
     */
    public Mono<Long> getCounter(String key) {
        return redissonReactiveClient.getAtomicLong(key)
                .get()
                .doOnSuccess(value -> log.debug("Get counter key: {}, value: {}", key, value))
                .doOnError(e -> log.error("Failed to get counter key: {}", key, e));
    }

    // ================================ 批量操作 ================================

    /**
     * 批量获取
     */
    public <T> Flux<T> multiGet(List<String> keys, Class<T> type) {
        return Flux.fromIterable(keys)
                .flatMap(key -> get(key, type))
                .doOnComplete(() -> log.debug("Multi get completed, keys count: {}", keys.size()))
                .doOnError(e -> log.error("Failed to multi get", e));
    }

    /**
     * 批量删除
     */
    public Mono<Long> multiDelete(List<String> keys) {
        return redissonReactiveClient.getKeys()
                .delete(keys.toArray(new String[0]))
                .doOnSuccess(count -> log.debug("Multi delete completed, deleted count: {}", count))
                .doOnError(e -> log.error("Failed to multi delete", e));
    }

    /**
     * 按模式删除键
     */
    public Mono<Long> deleteByPattern(String pattern) {
        return redissonReactiveClient.getKeys()
                .deleteByPattern(pattern)
                .doOnSuccess(count -> log.debug("Delete by pattern: {}, deleted count: {}", pattern, count))
                .doOnError(e -> log.error("Failed to delete by pattern: {}", pattern, e));
    }

    // ================================ 工具方法 ================================

    /**
     * 获取Bucket对象
     */
    private <T> RBucketReactive<T> getBucket(String key) {
        return redissonReactiveClient.getBucket(key);
    }

    /**
     * 获取List对象
     */
    private <T> RListReactive<T> getList(String key) {
        return redissonReactiveClient.getList(key);
    }

    /**
     * 获取Set对象
     */
    private <T> RSetReactive<T> getSet(String key) {
        return redissonReactiveClient.getSet(key);
    }

    /**
     * 获取Map对象
     */
    private <K, V> RMapReactive<K, V> getMap(String key) {
        return redissonReactiveClient.getMap(key);
    }

    /**
     * 获取数据库大小
     */
    public Mono<Long> dbSize() {
        return redissonReactiveClient.getKeys()
                .count()
                .doOnSuccess(size -> log.debug("Database size: {}", size))
                .doOnError(e -> log.error("Failed to get database size", e));
    }

    // ================================ 缓存模式操作 ================================

    /**
     * 缓存穿透保护 - 获取或计算值
     */
    public <T> Mono<T> getOrCompute(String key, Class<T> type, Mono<T> computation, Duration expireTime) {
        return get(key, type)
                .switchIfEmpty(computation
                        .flatMap(computed -> setWithExpire(key, computed, expireTime)
                                .thenReturn(computed)))
                .doOnSuccess(value -> log.debug("Get or compute key: {}", key))
                .doOnError(e -> log.error("Failed to get or compute key: {}", key, e));
    }

    /**
     * 刷新缓存
     */
    public <T> Mono<T> refreshCache(String key, T value, Duration expireTime) {
        return delete(key)
                .then(setWithExpire(key, value, expireTime))
                .thenReturn(value)
                .doOnSuccess(v -> log.debug("Refresh cache key: {}", key))
                .doOnError(e -> log.error("Failed to refresh cache key: {}", key, e));
    }
}