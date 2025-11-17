package io.osins.matrix.shared.cache.redis.constant;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.time.Duration;
import java.util.HashMap;

/**
 * 缓存名称常量
 */
@Slf4j
public final class DefaultCacheNameConstant {
    // 前缀
    public static final String CHECK_IN_PREFIX = "fxg:check-in:";
    public static final String CHECK_IN_RECORD = CHECK_IN_PREFIX + "record";
    public static final String CHECK_IN_TODAY = CHECK_IN_PREFIX + "today";
    public static final String CHECK_IN_CONFIG = CHECK_IN_PREFIX + "config";
    public static final String CHECK_IN_LADDER_REWARD = CHECK_IN_PREFIX + "ladder:reward";

    private DefaultCacheNameConstant() {
    }

    public static RedisCacheManager.RedisCacheManagerBuilder withInitialCacheConfigurations(RedisCacheConfiguration defaultConfig, RedisCacheManager.RedisCacheManagerBuilder builder) {
        var configs = new HashMap<String, RedisCacheConfiguration>();
        configs.put(DefaultCacheNameConstant.CHECK_IN_RECORD, defaultConfig.entryTtl(Duration.ofDays(7)));
        configs.put(DefaultCacheNameConstant.CHECK_IN_TODAY, defaultConfig.entryTtl(Duration.ofHours(24)));
        configs.put(DefaultCacheNameConstant.CHECK_IN_CONFIG, defaultConfig.entryTtl(Duration.ofHours(12)));
        configs.put(DefaultCacheNameConstant.CHECK_IN_LADDER_REWARD, defaultConfig.entryTtl(Duration.ofDays(30)));

        configs.forEach((key, value) -> log.debug("cacheName: {}, allow null: {}", key, value.getAllowCacheNullValues()));

        return builder.withInitialCacheConfigurations(configs);
    }
}
