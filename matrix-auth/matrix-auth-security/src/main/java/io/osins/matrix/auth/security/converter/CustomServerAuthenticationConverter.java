package io.osins.matrix.auth.security.converter;

import io.osins.matrix.auth.security.authentication.AnonymousAuthentication;
import io.osins.matrix.auth.security.authentication.JwtTokenAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomServerAuthenticationConverter implements ServerAuthenticationConverter {

    public static final String BEARER_PREFIX = "Bearer ";

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.just(Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
                .map(authHeader -> authHeader.substring(BEARER_PREFIX.length()))
                .filter(token -> !token.trim().isEmpty())
                .<Authentication>map(JwtTokenAuthentication::new)
                .orElse(new AnonymousAuthentication()));
    }
}