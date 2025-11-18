package io.osins.matrix.auth.security.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Slf4j
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        log.info("=== 初始化 CORS 配置 ===");

        var config = new CorsConfiguration();

        // 必须: 允许携带凭证
        config.setAllowCredentials(true);

        // 必须: 指定允许的源(不能用 * 当 credentials 为 true 时)
        config.addAllowedOrigin("http://localhost:8092");
        config.addAllowedOrigin("http://127.0.0.1:8092");

        // 允许所有请求头
        config.addAllowedHeader("*");

        // 允许所有请求方法
        config.addAllowedMethod("*");

        // 暴露响应头
        config.addExposedHeader("Set-Cookie");

        // 预检请求缓存
        config.setMaxAge(3600L);

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        log.info("CORS 配置完成,允许的源: {}", config.getAllowedOrigins());

        return new CorsWebFilter(source);
    }
}
