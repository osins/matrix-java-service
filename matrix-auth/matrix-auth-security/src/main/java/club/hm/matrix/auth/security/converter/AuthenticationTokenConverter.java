package club.hm.matrix.auth.security.converter;

import club.hm.matrix.auth.security.authentication.JwtTokenAuthentication;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class AuthenticationTokenConverter implements ServerAuthenticationConverter {

    public static final String BEARER_PREFIX = "Bearer ";
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.create(sink -> {
            var authentication = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                    .filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
                    .map(authHeader -> authHeader.substring(BEARER_PREFIX.length()))
                    .filter(token -> !token.isEmpty())
                    .map(JwtTokenAuthentication::new);
            authentication.ifPresentOrElse(sink::success, () -> sink.error(new RuntimeException("Invalid token")));
        });
    }
}