package club.hm.matrix.auth.oauth2.server.service;

import club.hm.matrix.shared.cache.redis.service.ReactiveRedissonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizationCodeService {
    public static final String OAUTH2_AUTHORIZATION_CODE = "oauth2-authorization-code";
    private static final Duration CODE_EXPIRE_TIME = Duration.ofMinutes(5); // 设置授权码过期时间为5分钟

    private final ReactiveRedissonService reactiveRedissonService;

    /**
     * 构建完整的Redis键
     *
     * @param code 授权码
     * @return 完整的键名
     */
    private String buildKey(String code) {
        return OAUTH2_AUTHORIZATION_CODE + ":" + code;
    }


    /**
     * 将 authorization code 存入缓存
     *
     * @param code  authorization code
     * @param state 客户端状态
     * @return Mono<String> 返回存储的state
     */
    public Mono<String> generate(String state) {
        var code = UUID.randomUUID().toString();
        return reactiveRedissonService.setWithExpire(buildKey(code), state, CODE_EXPIRE_TIME)
                .thenReturn(code)
                .doOnNext(c->log.info("生成授权码: {}", c))
                .doOnSuccess(v -> log.debug("Authorization code stored: {}", code))
                .doOnError(error -> log.error("Failed to store authorization code: {}", code, error));
    }

    /**
     * 从缓存中获取 authorization code 的状态
     *
     * @param code authorization code
     * @return Mono<String> 关联的 state，如果不存在返回 empty
     */
    public Mono<String> get(String code) {
        return reactiveRedissonService.get(buildKey(code), String.class)
                .doOnNext(state -> log.debug("Authorization code {} found: {}", code, state))
                .doOnError(error -> log.error("Failed to get authorization code: {}", code, error));
    }

    /**
     * 删除 authorization code
     *
     * @param code authorization code
     * @return Mono<Boolean> 删除是否成功
     */
    public Mono<Boolean> remove(String code) {
        return reactiveRedissonService.delete(buildKey(code))
                .doOnSuccess(deleted -> log.debug("Authorization code removed: {}, success: {}", code, deleted))
                .doOnError(error -> log.error("Failed to remove authorization code: {}", code, error));
    }

    /**
     * 验证并消费 authorization code
     * 这个方法会获取code对应的state，然后删除code（一次性使用）
     *
     * @param code authorization code
     * @return Mono<Optional<String>> 如果code有效返回对应的state，否则返回empty
     */
    public Mono<Boolean> validateAndConsume(String code, String state) {
        return get(code)
                .filter(state::equals)
                .flatMap(value -> {
                    return remove(code).flatMap(removed -> Mono.just(true));
                })
                .switchIfEmpty(Mono.just(false));
    }
}