package club.hm.matrix.shared.cache.redis.service;

import club.hm.matrix.shared.cache.redis.vo.CacheExpire;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Redis缓存服务接口
 * 定义了缓存操作的基本方法，用于在各个模块中进行缓存管理
 */
public interface IRedisCacheService<T extends Serializable> {
    /**
     * 获取缓存中的值
     * @param key 缓存键
     * @return 缓存值
     */
    T get(String key);

    /**
     * 设置缓存值
     * @param key 缓存键
     * @param value 缓存值
     * @param timeout 过期时间
     * @param unit 时间单位
     * @return 是否设置成功
     */
    boolean set(String key, T value, long timeout, TimeUnit unit);

    /**
     * 删除缓存中的值
     * @param key 缓存键
     * @return 是否删除成功
     */
    boolean delete(String key);

    CacheExpire getExpire(String key);

    /**
     * 判断缓存中是否包含指定的键
     * @param key 缓存键
     * @return 是否存在
     */
    boolean contains(String key);

    boolean changeExpire(String key, long timeout, TimeUnit unit);

    boolean changeExpireAt(String key, Date expireAt);

    /**
     * 缓存数据访问方法
     * @param key 缓存键值
     * @param isCache 是否启用缓存
     * @param supplier 数据供应函数，当需要重新加载数据时调用
     * @return 缓存的数据对象
     */
    T cache(String key, boolean isCache, Supplier<T> supplier);

    T cache(String key, boolean isCache, Supplier<T> supplier, long timeout, TimeUnit unit);
}