package club.hm.matrix.auth.security.cache;

import club.hm.matrix.shared.cache.redis.service.IRedisCacheService;
import club.hm.matrix.shared.cache.redis.service.impl.AbstractRedisCacheService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public class SmsLoginCodeCache extends AbstractRedisCacheService<String> implements IRedisCacheService<String> {
    @Autowired
    public SmsLoginCodeCache(RedisTemplate<String, String> redisStringTemplate) {
        super(redisStringTemplate);
    }

    private String getKeyByPrefix(String key) {
        return "fxg:security:sms:login:code:" + Optional.ofNullable(key).orElseThrow(() -> new RuntimeException("短信登录参数错误"));
    }

    public Optional<String> generate(String mobile) {
        var code = RandomStringUtils.random(4, "123456789");
        return set(mobile, code) ? Optional.of(code) : Optional.empty();
    }

    private boolean set(String mobile, String code) {
        return super.set(getKeyByPrefix(mobile), code, 1, TimeUnit.MINUTES);
    }

    public Optional<String> getCode(String mobile) {
        return Optional.ofNullable(super.get(getKeyByPrefix(mobile)));
    }
}
