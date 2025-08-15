package club.hm.matrix.auth.security.filter;

import org.springframework.lang.NonNull;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.web.server.authorization.AuthorizationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class CustomAuthorizationWebFilter extends AuthorizationWebFilter implements WebFilter {
    public CustomAuthorizationWebFilter(ReactiveAuthorizationManager<? super ServerWebExchange> authorizationManager) {
        super(authorizationManager);
    }

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange,@NonNull WebFilterChain chain) {
        return super.filter(exchange, chain);
    }
}
