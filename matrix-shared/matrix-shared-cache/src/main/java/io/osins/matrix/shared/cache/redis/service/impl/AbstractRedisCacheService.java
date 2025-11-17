package io.osins.matrix.shared.cache.redis.service.impl;

import io.osins.matrix.shared.cache.redis.service.IRedisCacheService;
import io.osins.matrix.shared.cache.redis.vo.CacheExpire;
import com.google.common.base.Strings;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Redis缓存服务实现类
 *
 * @param <T> 缓存值的类型，必须为可序列化对象
 */
@Slf4j
@Service
@RequiredArgsConstructor
public abstract class AbstractRedisCacheService<T extends Serializable> implements IRedisCacheService<T> {
    private static final long DEFAULT_EXPIRE_SECONDS = 60 * 60 * 24 * 15;

    private final RedisTemplate<String, T> redisTemplate;

    /**
     * 从Redis中获取缓存数据
     *
     * @param key Redis键值
     * @return 缓存数据，如果不存在或发生异常则返回null
     * @throws SerializationException 当反序列化失败时抛出
     */
    @Override
    public T get(String key) {
        try {
            if (!contains(key)) {
                log.warn("Redis缓存中不存在key:{}", key);
                return null;
            }

            return redisTemplate.opsForValue().get(key);
        } catch (SerializationException e) {
            // 当反序列化失败时清除无效缓存
            log.error("Redis缓存服务异常：{}, 已存在的缓存将被清除", e.getMessage(), e);
            redisTemplate.delete(key);
            return null;
        } catch (Exception e) {
            log.error("Redis缓存服务异常：{}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 将键值对存储到Redis中，并设置过期时间
     *
     * @param key     存储的键
     * @param value   存储的值
     * @param timeout 键的过期时间
     * @param unit    过期时间的时间单位
     * @return 操作是否成功
     */
    @Override
    public boolean set(String key, T value, long timeout, TimeUnit unit) {
        try {
            // 处理空值情况，提前删除无效缓存
            if (value == null) {
                log.warn("缓存数据保存失败, value为null, key: {}", key);
                redisTemplate.delete(key);
                return false;
            }

            // 处理空字符串情况
            if (value instanceof String && Strings.isNullOrEmpty((String) value)) {
                log.warn("缓存数据保存失败, value为null或空字符串, key: {}", key);
                redisTemplate.delete(key);
                return false;
            }

            redisTemplate.opsForValue().set(key, value, timeout, unit);

            return true;
        } catch (Exception e) {
            log.error("缓存数据保存失败, key: {}, value: {}", key, value, e);
            return false;
        }
    }

    /**
     * 删除指定的缓存键值对
     *
     * @param key 要删除的键
     * @return 删除操作是否成功
     */
    @Override
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     * 获取指定键的缓存过期时间
     *
     * @param key 缓存键
     * @return CacheExpire 缓存过期时间对象
     */
    @Override
    public CacheExpire getExpire(String key) {
        return CacheExpire.of(redisTemplate.getExpire(key));
    }

    /**
     * 检查缓存中是否存在指定键
     *
     * @param key 要检查的键
     * @return 键是否存在
     */
    @Override
    public boolean contains(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("判断缓存是否存在时发生异常, key: {}", key, e);
            return false;
        }
    }

    /**
     * 为已存在的键单独设置过期时间
     * 与set方法不同，此方法仅设置过期时间
     *
     * @param key     存储的键
     * @param timeout 键的过期时间
     * @param unit    过期时间的时间单位
     * @return 操作是否成功
     */
    @Override
    public boolean changeExpire(String key, long timeout, TimeUnit unit) {
        // 使用EXPIRE命令单独设置过期时间
        try {
            return redisTemplate.expire(key, timeout, unit);
        } catch (Exception e) {
            log.warn("changeExpire error: {}", e.getMessage(), e);
            redisTemplate.delete(key);
            return false;
        }
    }

    /**
     * 更改给定键的过期绝对时间
     *
     * @param key      Redis中的键
     * @param expireAt 绝对时间
     * @return 如果设置过期时间成功，则返回true；否则返回false
     */
    @Override
    public boolean changeExpireAt(String key, Date expireAt) {
        return redisTemplate.expireAt(key, expireAt);
    }

    /**
     * 缓存数据获取与加载模板方法
     * 优先从缓存获取数据，缓存未命中则通过Supplier加载数据并重新设置缓存
     *
     * @param key      存储的键
     * @param isCache  是否启用缓存
     * @param supplier 数据提供者
     * @return 缓存中的数据或新获取的数据
     */
    @Override
    public T cache(String key, boolean isCache, Supplier<T> supplier) {
        return cache(key, isCache, supplier, DEFAULT_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 带自定义过期时间的缓存数据获取与加载模板方法
     * 优先从缓存获取数据，缓存未命中则通过Supplier加载数据并按指定时间设置缓存
     *
     * @param key      存储的键
     * @param isCache  是否启用缓存
     * @param supplier 数据提供者
     * @param timeout  自定义过期时间
     * @param unit     时间单位
     * @return 缓存中的数据或新获取的数据
     */
    @Override
    public T cache(String key, boolean isCache, Supplier<T> supplier, long timeout, TimeUnit unit) {
        if (isCache && contains(key)) {
            try {
                return get(key);
            } catch (Exception ex) {
                // 获取缓存失败时记录日志并尝试删除无效缓存
                log.error("缓存数据获取失败, key: {}", key, ex);
                try {
                    delete(key);
                } catch (Exception delEx) {
                    log.warn("缓存删除失败, key: {}", key, delEx);
                }
            }
        }

        var value = supplier.get();

        set(key, value, timeout, unit);
        return value;
    }

    /**
     * 缓存过期状态枚举
     * 定义了缓存键的多种过期状态类型
     * <p>
     * NONE(-999): 未知状态
     * NOT_SETTING(-1): 未设置过期时间
     * NOT_EXPIRED(1): 未过期
     * NOT_EXIST(-2): 键不存在
     *
     * @param code    状态编码
     * @param message 状态描述
     * @return 构造完成的CacheExpireStatus实例
     */
    @Getter
    public enum CacheExpireStatus {
        NONE(-999, "未知"),
        NOT_SETTING(-1, "未设置"),
        NOT_EXPIRED(1, "未过期"),
        NOT_EXIST(-2, "不存在");

        CacheExpireStatus(long code, String message) {
            this.code = code;
            this.message = message;
        }

        private final long code;
        private final String message;

        /**
         * 根据过期时间转换为对应的状态枚举
         *
         * @param expire 过期时间（毫秒）
         * @return 对应的状态枚举
         */
        public static CacheExpireStatus of(long expire) {
            if (expire >= 0) {
                return NOT_EXPIRED;
            }

            if (expire == -1L) {
                return NOT_SETTING;
            } else if (expire == -2L) {
                return NOT_EXIST;
            }

            return NONE;
        }

        /**
         * 判断当前缓存是否处于已存在状态
         * <p>
         * 此方法通过比较当前对象与预定义的 NOT_EXIST 对象来判断当前对象是否存在
         * 如果当前对象不等于 NOT_EXIST，则认为当前对象存在
         *
         * @return 如果当前对象存在，则返回 true；否则返回 false
         */
        public boolean exists() {
            return !NOT_EXIST.equals(this);
        }
    }
}