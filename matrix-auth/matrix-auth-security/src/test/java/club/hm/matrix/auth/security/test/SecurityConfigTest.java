package club.hm.matrix.auth.security.test;

import club.hm.matrix.auth.security.authentication.CustomReactiveAuthenticationManager;
import club.hm.matrix.auth.security.authentication.CustomReactiveAuthorizationManager;
import club.hm.matrix.auth.security.converter.CustomServerAuthenticationConverter;
import club.hm.matrix.auth.security.domain.CustomPrincipal;
import club.hm.matrix.auth.security.domain.JwtSetting;
import club.hm.matrix.auth.security.enums.JWTokenType;
import club.hm.matrix.auth.security.filter.CustomAuthenticationWebFilter;
import club.hm.matrix.auth.security.filter.CustomAuthorizationWebFilter;
import club.hm.matrix.auth.security.filter.CustomServerAuthenticationFailureHandler;
import club.hm.matrix.auth.security.handler.CustomAuthenticationSuccessHandler;
import club.hm.matrix.auth.security.jwt.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@Slf4j
@SpringJUnitConfig
@SpringBootTest(classes = TestApplication.class)
class SecurityConfigTest {
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CustomReactiveAuthorizationManager accessDecisionManager;

    @Autowired
    private CustomReactiveAuthenticationManager authenticationManager;

    @Autowired
    private CustomServerAuthenticationConverter tokenConverter;

    @Autowired
    private CustomAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private CustomServerAuthenticationFailureHandler authenticationFailureHandler;

    private CustomAuthenticationWebFilter getAuthenticationWebFilter() {
        var filter = new CustomAuthenticationWebFilter(authenticationManager);
        filter.setServerAuthenticationConverter(tokenConverter);
        filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler);
        return filter;
    }

    @Test
    void testFilter() {
        var jwtSettings = JwtSetting.create()
                .setJti(UUID.randomUUID().toString())
                .setType(JWTokenType.ACCESS_TOKEN)
                .setSubject(CustomPrincipal.builder().username("wahaha").build());

        var token = jwtTokenProvider.generateToken(jwtSettings, JWTokenType.ACCESS_TOKEN);

//        testSuccess(token);
//        testFail("xxxxdfdsfs");

        testSuccess2(token);
        testFail2("xxxxdfdsfs");
    }

    void testSuccess(String token){
        log.info("token: {}", token);

        // 创建 mock 请求和交换对象
        var request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        var exchange = MockServerWebExchange.from(request);

        var filter = new CustomAuthorizationWebFilter(accessDecisionManager);
        // 模拟 WebFilterChain
        WebFilterChain chain = e -> Mono.empty();

        // 调用 filter
        var result = filter.filter(exchange, chain).onErrorResume(throwable -> {
            log.error("测试正确的令牌失败: {}", throwable.getMessage(), throwable);
            return Mono.empty();
        });

        // 验证 Mono 执行完成
        StepVerifier.create(result).verifyComplete();
    }

    void testFail(String token){
        log.info("token: {}", token);

        // 创建 mock 请求和交换对象
        var request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        var exchange = MockServerWebExchange.from(request);

        var filter = new CustomAuthorizationWebFilter(accessDecisionManager);
        // 模拟 WebFilterChain
        WebFilterChain chain = e -> Mono.empty();

        // 调用 filter
        var result = filter.filter(exchange, chain)
                .onErrorResume(throwable -> {
                    log.info("测试错误的令牌成功: {}", throwable.getMessage(), throwable);
                    throw new RuntimeException(throwable);
                });

        // 验证 Mono 执行完成
        StepVerifier.create(result).verifyError();
    }

    void testSuccess2(String token){
        log.info("token: {}", token);

        // 创建 mock 请求和交换对象
        var request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        var exchange = MockServerWebExchange.from(request);

        // 模拟 WebFilterChain
        WebFilterChain chain = e -> Mono.empty();

        // 调用 filter
        var result = getAuthenticationWebFilter().filter(exchange, chain).onErrorResume(throwable -> {
            log.error("测试正确的令牌失败: {}", throwable.getMessage(), throwable);
            return Mono.empty();
        });

        // 验证 Mono 执行完成
        StepVerifier.create(result).verifyComplete();
    }

    void testFail2(String token){
        log.info("token: {}", token);

        // 创建 mock 请求和交换对象
        var request = MockServerHttpRequest.get("/api/test")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .build();
        var exchange = MockServerWebExchange.from(request);

        // 模拟 WebFilterChain
        WebFilterChain chain = e -> Mono.empty();

        // 调用 filter
        var result = getAuthenticationWebFilter().filter(exchange, chain)
                .onErrorResume(throwable -> {
                    log.info("测试错误的令牌成功: {}", throwable.getMessage(), throwable);
                    throw new RuntimeException(throwable);
                });

        // 验证 Mono 执行完成
        StepVerifier.create(result).verifyError();
    }
}