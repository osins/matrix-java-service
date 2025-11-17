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
        var requestId = RequestIdWebFilter.getRequestId(exchange);

        log.debug("CustomAuthenticationWebFilter, request id: {}", requestId);

        // 可选：添加必要的 CORS 响应头
        var headers = exchange.getResponse().getHeaders();
        if (!headers.containsKey("Access-Control-Allow-Origin")) {
            headers.add("Access-Control-Allow-Origin", "*");
        }

        if (!headers.containsKey("Access-Control-Allow-Methods")) {
            headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        }

        if (!headers.containsKey("Access-Control-Allow-Headers")) {
            headers.add("Access-Control-Allow-Headers", "*");
        }

        // 如果是 OPTIONS 请求，直接返回 200
        if (HttpMethod.OPTIONS.equals(exchange.getRequest().getMethod())) {
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.OK);

            return exchange.getResponse().setComplete();
        }

        return super.filter(exchange, chain)
                .contextWrite(Context.of(
                        ContextKeys.REQUEST_ID, requestId,
                        ContextKeys.REQUEST_TIME, System.currentTimeMillis()
                ));
    }
}
