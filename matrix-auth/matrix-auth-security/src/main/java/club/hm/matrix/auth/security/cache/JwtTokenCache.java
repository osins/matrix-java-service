package club.hm.matrix.auth.security.cache;

import club.hm.homemart.club.shared.common.Exception.CustomException;
import club.hm.matrix.auth.security.config.SecurityJwtConfig;
import club.hm.matrix.auth.security.domain.JtiSet;
import club.hm.matrix.shared.cache.redis.service.impl.AbstractRedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Component
public class JwtTokenCache extends AbstractRedisCacheService<JtiSet> {
    private final SecurityJwtConfig jwtConfig;


    @Autowired
    public JwtTokenCache(RedisTemplate<String, JtiSet> redisTemplate, SecurityJwtConfig jwtConfig0) {
        super(redisTemplate);

        this.jwtConfig = jwtConfig0;
    }

    /**
     * 生成缓存键
     *
     * @param i       分页索引参数
     * @param s       分页大小参数
     * @param keyword 搜索关键词
     * @return 生成的缓存键字符串
     */
    private String key(Long userId) {
        return getKeyPrefix() + ":" + Optional.ofNullable(userId).orElseThrow(() -> new CustomException("jwt token 缓存 jti 不能为空"));
    }

    /**
     * 获取或创建缓存数据
     *
     * @param data    分页信息对象
     * @param keyword 搜索关键词
     * @param isCache 是否使用缓存标志
     * @param call    实际获取数据的回调函数
     * @return 返回IPage<DigitalAssetsMember>类型的缓存数据
     */
    public JtiSet get(Long userId, String jti, boolean isCache, Function<String, JtiSet> call) {
        return cache(key(userId), isCache, () -> call.apply(jti), jwtConfig.getExpiration(), TimeUnit.SECONDS);
    }

    public boolean add(Long userId, String jti) {
        var jtis = Optional.ofNullable(get(key(userId))).orElse(new JtiSet());
        var returnValue = jtis.add(jti);
        set(key(userId), jtis, jwtConfig.getExpiration(), TimeUnit.SECONDS);

        return returnValue;
    }

    public boolean add(Long userId, JtiSet jtis) {
        var cacheJtis = Optional.ofNullable(get(key(userId))).orElse(new JtiSet());
        var returnValue = cacheJtis.addAll(jtis);

        set(key(userId), cacheJtis, jwtConfig.getExpiration(), TimeUnit.SECONDS);

        return returnValue;
    }

    public void remove(Long userId, String jti) {
        if (!contains(key(userId))) {
            return;
        }

        var jtis = get(key(userId));
        var returnValue = jtis.remove(jti);
        if (returnValue)
            set(key(userId), jtis, jwtConfig.getExpiration(), TimeUnit.SECONDS);

    }

    public boolean remove(Long userId, JtiSet jtiSet) {
        if (!contains(key(userId))) {
            return false;
        }

        var jtis = get(key(userId));
        var returnValue = jtis.removeAll(jtiSet);
        if (returnValue) {
            jtiSet.forEach(jti -> remove(userId, jti));
            set(key(userId), jtis, jwtConfig.getExpiration(), TimeUnit.SECONDS);
        }

        return returnValue;
    }

    public boolean remove(Long userId) {
        if (!contains(key(userId))) {
            return false;
        }

        return super.delete(key(userId));
    }

    public boolean contains(Long userId, String jti) {
        if (!super.contains(key(Optional.ofNullable(userId).orElseThrow(() -> new CustomException("jwt token 缓存 uid 不能为空"))))) {
            return false;
        }

        return Optional.ofNullable(get(key(userId)))
                .filter(jtis -> !jtis.isEmpty())
                .filter(jtis -> jtis.contains(Optional.ofNullable(jti).orElseThrow(() -> new CustomException("jwt token 缓存 jti 不能为空"))))
                .isPresent();
    }

//    @Override
    public String getKeyPrefix() {
        return "security:jwt:token:uid:";
    }
}
