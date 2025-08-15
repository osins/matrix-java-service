package club.hm.matrix.shared.cache.redis.config;

import club.hm.matrix.shared.cache.redis.constant.DefaultCacheNameConstant;
import club.hm.matrix.shared.cache.redis.interfaces.InitialCacheConfigurations;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;

@Slf4j
@Configuration
public class DefaultInitialCacheConfigurations implements InitialCacheConfigurations {
    @Override
    public RedisCacheManager.RedisCacheManagerBuilder withInitialCacheConfigurations(RedisCacheConfiguration defaultConfig, RedisCacheManager.RedisCacheManagerBuilder builder) {
        return DefaultCacheNameConstant.withInitialCacheConfigurations(defaultConfig, builder);
    }
}
