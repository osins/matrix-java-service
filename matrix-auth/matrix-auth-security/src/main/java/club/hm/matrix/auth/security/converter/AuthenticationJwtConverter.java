package club.hm.matrix.auth.security.converter;

import club.hm.matrix.auth.security.domain.CustomPrincipal;
import club.hm.matrix.auth.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationJwtConverter implements ServerAuthenticationConverter {

    public static final String BEARER_PREFIX = "Bearer ";
    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.create(sink -> {
            var authentication = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                    .filter(authHeader -> authHeader.startsWith(BEARER_PREFIX))
                    .map(authHeader -> authHeader.substring(BEARER_PREFIX.length()))
                    .filter(token -> !token.isEmpty())
                    .map(this::convert);
            authentication.ifPresentOrElse(sink::success, () -> sink.error(new RuntimeException("Invalid token")));
        });
    }

    public Authentication convert(String token) {
        var claimsJws = tokenProvider.decryptToken(token);
        var subject = claimsJws.getPayload().getSubject();
        var principal = tokenToPrincipal(subject);
        if(principal==null)
            throw new RuntimeException("Invalid token");

        return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
    }

    private CustomPrincipal tokenToPrincipal(String subject) {
        try {
            return objectMapper.readValue(subject, CustomPrincipal.class);
        } catch (Exception ex) {
            log.error("tokenToPrincipal error: {}, {}", subject, ex.getMessage(), ex);
            return null;
        }
    }
}