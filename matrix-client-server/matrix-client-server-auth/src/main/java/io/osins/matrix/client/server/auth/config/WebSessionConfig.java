package io.osins.matrix.client.server.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.server.EnableRedisWebSession;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.session.CookieWebSessionIdResolver;
import org.springframework.web.server.session.WebSessionIdResolver;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableRedisWebSession(maxInactiveIntervalInSeconds = 3600)
public class WebSessionConfig {
    @Bean
    public WebSessionIdResolver webSessionIdResolver() {
        var resolver = new CookieWebSessionIdResolver();
        resolver.setCookieName("SESSION");
        resolver.addCookieInitializer(builder -> {
            builder.path("/");
            builder.httpOnly(true);
            builder.sameSite("Lax");
            builder.maxAge(java.time.Duration.ofHours(1));
        });
        log.info("WebSessionIdResolver 配置完成,Cookie 名称: SESSION");
        return resolver;
    }

    @Bean
    public WebFilter sessionDebugFilter() {
        return (exchange, chain) -> {
            return exchange.getSession().flatMap(session -> {
                log.debug("=== Session Debug Info ===");
                log.debug("Session ID: {}", session.getId());
                log.debug("Session Attributes: {}", session.getAttributes());
                log.debug("Request Cookies: {}", exchange.getRequest().getCookies());
                log.debug("Request Origin: {}", exchange.getRequest().getHeaders().getOrigin());
                log.debug("========================");

                return chain.filter(exchange)
                        .then(session.save())
                        .then(Mono.fromRunnable(() -> {
                            log.debug("=== After Request ===");
                            log.debug("Session ID: {}", session.getId());
                            log.debug("Session Attributes: {}", session.getAttributes());

                            // 关键: 检查响应头
                            var responseCookies = exchange.getResponse().getCookies();
                            var responseHeaders = exchange.getResponse().getHeaders();

                            log.debug("Response Set-Cookie: {}", responseCookies.get("SESSION"));
                            log.debug("Response Access-Control-Allow-Origin: {}",
                                    responseHeaders.getAccessControlAllowOrigin());
                            log.debug("Response Access-Control-Allow-Credentials: {}",
                                    responseHeaders.getAccessControlAllowCredentials());
                            log.debug("====================");
                        }));
            });
        };
    }
}
