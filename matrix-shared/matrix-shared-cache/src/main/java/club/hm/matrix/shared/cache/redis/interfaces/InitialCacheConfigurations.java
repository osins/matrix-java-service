package club.hm.matrix.shared.cache.redis.interfaces;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;

public interface InitialCacheConfigurations {
    /**
     * 初始化默认缓存配置
     * @param defaultConfig 默认缓存配置
     * @param builder 缓存管理器构建器
     * @return 缓存管理器构建器
     */
    RedisCacheManager.RedisCacheManagerBuilder withInitialCacheConfigurations(RedisCacheConfiguration defaultConfig, RedisCacheManager.RedisCacheManagerBuilder builder);
}
