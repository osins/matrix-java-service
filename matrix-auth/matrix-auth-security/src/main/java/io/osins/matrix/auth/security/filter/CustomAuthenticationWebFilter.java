package io.osins.matrix.auth.security.filter;

import io.osins.matrix.auth.security.context.ContextKeys;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Slf4j
public class CustomAuthenticationWebFilter extends AuthenticationWebFilter implements WebFilter {
    public CustomAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        var requestId = io.osins.matrix.auth.security.filter.RequestIdUtils.getRequestId(exchange);

        log.debug("CustomAuthenticationWebFilter, request id: {}", requestId);

        return super.filter(exchange, chain)
                .contextWrite(Context.of(
                        ContextKeys.REQUEST_ID, requestId,
                        ContextKeys.REQUEST_TIME, System.currentTimeMillis()
                ));
    }
}
