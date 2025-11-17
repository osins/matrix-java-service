package io.osins.matrix.shared.cache.redis.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomCacheErrorHandler implements CacheErrorHandler {
    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        if(exception instanceof SerializationException){
            log.error("缓存获取,序列化异常: {}, {}", key,  exception.getMessage(), exception);
            cache.put(key, null);
        }
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        if(exception instanceof SerializationException && value!=null){
            log.error("缓存推送,序列化异常: {}, {}, {}", key,  exception.getMessage(), value, exception);
            cache.put(key, null);
        }
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        if(exception instanceof SerializationException){
            log.error("缓存回收,序列化异常: {}, {}", key,  exception.getMessage(), exception);
            cache.put(key, null);
        }
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.error("缓存清除,异常: {}", exception.getMessage(), exception);
    }
}
