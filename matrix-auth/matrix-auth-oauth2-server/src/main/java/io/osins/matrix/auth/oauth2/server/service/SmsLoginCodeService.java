package io.osins.matrix.auth.oauth2.server.service;

import io.osins.shared.common.uitls.Random;
import io.osins.matrix.shared.cache.redis.service.ReactiveRedissonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsLoginCodeService {
    public static final String SMS_LOGIN_CODE = "sms-login-code";
    private final ReactiveRedissonService reactiveRedissonService;

    private String key(String mobile) {
        return String.format("%s:%s", SMS_LOGIN_CODE, mobile);
    }

    /**
     * 生成短信验证码 - WebFlux版本
     *
     * @param mobile 手机号码
     * @return Mono<Optional<String>> 生成的验证码
     */
    public Mono<String> generate(String mobile) {
        return exists(mobile).flatMap(returnValue -> {
                    if (returnValue)
                        return Mono.error(new RuntimeException("验证码已发送,请不要重复请求"));

                    // 生成6位数字验证码
                    var code = Random.number(6);
                    log.debug("生成验证码: {}", code);

                    return reactiveRedissonService.set(key(mobile), code)
                            .thenReturn(code);
                });
    }

    /**
     * 获取短信验证码 - WebFlux版本
     *
     * @param mobile 手机号码
     * @return Mono<Optional<String>> 验证码，如果不存在返回empty
     */
    public Mono<String> getCode(String mobile) {
        return reactiveRedissonService.get(key(mobile), String.class);
    }

    public Mono<Boolean> exists(String mobile) {
        return reactiveRedissonService.exists(key(mobile));
    }


    /**
     * 删除短信验证码 - WebFlux版本
     *
     * @param mobile 手机号码
     * @return Mono<Boolean> 删除是否成功
     */
    public Mono<Boolean> remove(String mobile) {
        return reactiveRedissonService.delete(key(mobile));
    }

    /**
     * 验证并消费短信验证码 - WebFlux版本
     *
     * @param mobile 手机号码
     * @param code   验证码
     * @return Mono<Boolean> 验证是否成功
     */
    public Mono<Boolean> validateAndConsume(String mobile, String code) {
        return getCode(mobile)
                .filter(existsCode -> existsCode.equals(code))
                .flatMap(existsCode -> remove(mobile));
    }
}