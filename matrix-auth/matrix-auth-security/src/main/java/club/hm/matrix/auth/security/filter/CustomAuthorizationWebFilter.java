package club.hm.matrix.auth.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static club.hm.homemart.club.shared.common.log.MDCContext.REQUEST_ID_KEY;

@Slf4j
public class CustomAuthorizationWebFilter extends AuthorizationWebFilter implements WebFilter {
    public CustomAuthorizationWebFilter(ReactiveAuthorizationManager<? super ServerWebExchange> authorizationManager) {
        super(authorizationManager);
    }

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        log.debug("CustomAuthorizationWebFilter, request id: {}", RequestIdWebFilter.getRequestId(exchange));

        return super.filter(exchange, chain);
    }
}
