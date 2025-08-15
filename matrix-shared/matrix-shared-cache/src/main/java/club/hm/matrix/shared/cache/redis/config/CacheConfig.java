package club.hm.matrix.shared.cache.redis.config;

import club.hm.matrix.shared.cache.redis.handler.CustomCacheErrorHandler;
import club.hm.matrix.shared.cache.redis.interfaces.InitialCacheConfigurations;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.CharMatcher;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * 缓存配置类
 */
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig implements CachingConfigurer {
    private final RedisConnectionFactory connectionFactory;
    private final InitialCacheConfigurations initialCacheConfigurations;

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            // 规则示例：类名 + 方法名 + 参数哈希
            var key = new StringBuilder();
            key.append("fxg")
                    .append(":")
                    .append(target.getClass().getSimpleName())
                    .append(".")
                    .append(method.getName())
                    .append(":");

            // 拼接参数（简单实现）
            for (Object param : params) {
                key.append(param != null ? param.hashCode() : "x");
                key.append(",");
            }

            return CharMatcher.is(',').trimTrailingFrom(key.toString());
        };
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

    @Bean
    public CacheManager cacheManager() {
        // 自定义Jackson序列化配置
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());  // 支持Java8时间类型

        // 禁用日期转时间戳（Jackson 2.9.x兼容写法）
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        var ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfSubType(Object.class) // 可以根据需要限制特定包/类
                .build();


        // 启用默认类型信息（使用WRAPPER_ARRAY格式进行序列化）
        objectMapper.activateDefaultTyping(
                ptv,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        // 默认配置
        var defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)));

        return initialCacheConfigurations.withInitialCacheConfigurations(defaultConfig, RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig))
                .build();
    }
}
