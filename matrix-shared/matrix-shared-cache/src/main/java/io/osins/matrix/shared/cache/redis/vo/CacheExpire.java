package io.osins.matrix.shared.cache.redis.vo;

import io.osins.matrix.shared.cache.redis.service.impl.AbstractRedisCacheService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 缓存过期策略配置类
 * 用于封装缓存过期时间和对应状态标识
 *
 * @param expire 指定的过期时间（毫秒）
 * @param status 关联的过期状态枚举
 * @return 构造完成的CacheExpire实例
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CacheExpire {
    private long expire;
    private AbstractRedisCacheService.CacheExpireStatus status;

    /**
     * 工厂方法创建缓存过期策略
     *
     * @param expire 指定的过期时间（毫秒）
     * @return 新建的CacheExpire实例
     */
    public static CacheExpire of(long expire) {
        return new CacheExpire(expire, AbstractRedisCacheService.CacheExpireStatus.of(expire));
    }
}
