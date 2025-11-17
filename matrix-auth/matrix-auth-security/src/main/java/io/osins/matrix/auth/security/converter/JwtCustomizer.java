package io.osins.matrix.auth.security.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtCustomizer implements Customizer<ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec> {
    private final CustomReactiveJwtDecoder decoder;

    @Override
    public void customize(ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwtSpec) {
        // 设置自定义 JwtDecoder
        jwtSpec.jwtDecoder(decoder);

        // 设置自定义 JwtAuthenticationConverter
        jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter());
    }

    private Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        // 返回自定义 JWT -> Authentication 转换器
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        // 自定义转换逻辑，比如设置 authorities
        return new ReactiveJwtAuthenticationConverterAdapter(converter);
    }
}
