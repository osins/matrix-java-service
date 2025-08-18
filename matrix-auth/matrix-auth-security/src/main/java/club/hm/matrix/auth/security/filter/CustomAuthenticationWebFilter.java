package club.hm.matrix.auth.security.filter;

import lombok.NonNull;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

public class CustomAuthenticationWebFilter extends AuthenticationWebFilter implements WebFilter {
    public CustomAuthenticationWebFilter(ReactiveAuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public @NonNull Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        return super.filter(exchange, chain);
    }
}
