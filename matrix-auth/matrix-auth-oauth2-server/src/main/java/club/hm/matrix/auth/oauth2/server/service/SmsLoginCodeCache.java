package club.hm.matrix.auth.oauth2.server.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SmsLoginCodeCache {
    public static final String SMS_LOGIN_CODE = "sms-login-code";
    private static final int CODE_LENGTH = 6;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final CacheManager cacheManager;

    /**
     * 生成短信验证码 - WebFlux版本
     *
     * @param mobile 手机号码
     * @return Mono<Optional<String>> 生成的验证码
     */
    public Mono<Optional<String>> generateAsync(String mobile) {
        return Mono.fromCallable(() -> {
                    // 生成6位数字验证码
                    StringBuilder code = new StringBuilder();
                    for (int i = 0; i < CODE_LENGTH; i++) {
                        code.append(RANDOM.nextInt(10));
                    }

                    String smsCode = code.toString();
                    Cache cache = cacheManager.getCache(SMS_LOGIN_CODE);
                    if (cache != null) {
                        cache.put(mobile, smsCode);
                        log.debug("SMS code generated for mobile: {}", mobile);
                        return Optional.of(smsCode);
                    }

                    log.error("Cache not available for SMS code generation");
                    return Optional.<String>empty();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(error -> log.error("Failed to generate SMS code for mobile: {}", mobile, error))
                .onErrorReturn(Optional.empty());
    }

    /**
     * 获取短信验证码 - WebFlux版本
     *
     * @param mobile 手机号码
     * @return Mono<Optional<String>> 验证码，如果不存在返回empty
     */
    public Mono<Optional<String>> getCodeAsync(String mobile) {
        return Mono.fromCallable(() -> {
                    Cache cache = cacheManager.getCache(SMS_LOGIN_CODE);
                    if (cache != null) {
                        Cache.ValueWrapper wrapper = cache.get(mobile);
                        if (wrapper != null && wrapper.get() != null) {
                            String code = (String) wrapper.get();
                            log.debug("SMS code found for mobile: {}", mobile);
                            return Optional.of(code);
                        }
                    }
                    log.debug("SMS code not found for mobile: {}", mobile);
                    return Optional.<String>empty();
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(error -> log.error("Failed to get SMS code for mobile: {}", mobile, error))
                .onErrorReturn(Optional.empty());
    }

    /**
     * 删除短信验证码 - WebFlux版本
     *
     * @param mobile 手机号码
     * @return Mono<Boolean> 删除是否成功
     */
    public Mono<Boolean> removeCodeAsync(String mobile) {
        return Mono.fromCallable(() -> {
                    Cache cache = cacheManager.getCache(SMS_LOGIN_CODE);
                    if (cache != null) {
                        cache.evict(mobile);
                        log.debug("SMS code removed for mobile: {}", mobile);
                        return true;
                    }
                    return false;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .doOnError(error -> log.error("Failed to remove SMS code for mobile: {}", mobile, error))
                .onErrorReturn(false);
    }

    /**
     * 验证并消费短信验证码 - WebFlux版本
     *
     * @param mobile 手机号码
     * @param code 验证码
     * @return Mono<Boolean> 验证是否成功
     */
    public Mono<Boolean> validateAndConsumeCodeAsync(String mobile, String code) {
        return getCodeAsync(mobile)
                .flatMap(cachedCodeOpt -> {
                    if (cachedCodeOpt.isPresent() && cachedCodeOpt.get().equals(code)) {
                        return removeCodeAsync(mobile)
                                .map(removed -> {
                                    if (removed) {
                                        log.debug("SMS code validated and consumed for mobile: {}", mobile);
                                        return true;
                                    } else {
                                        log.warn("Failed to remove SMS code after validation for mobile: {}", mobile);
                                        return false;
                                    }
                                });
                    } else {
                        log.debug("SMS code validation failed for mobile: {}", mobile);
                        return Mono.just(false);
                    }
                });
    }

    // 为了向后兼容，保留同步方法（但建议使用异步版本）
    @Deprecated
    public Optional<String> generate(String mobile) {
        return generateAsync(mobile).block();
    }

    @Deprecated
    public Optional<String> getCode(String mobile) {
        return getCodeAsync(mobile).block();
    }

    @Deprecated
    public void removeCode(String mobile) {
        removeCodeAsync(mobile).block();
    }
}