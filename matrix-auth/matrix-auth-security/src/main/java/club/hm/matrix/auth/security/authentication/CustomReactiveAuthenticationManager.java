package club.hm.matrix.auth.security.authentication;

import club.hm.matrix.auth.security.domain.CustomPrincipal;
import club.hm.matrix.auth.security.exception.CustomAuthenticationException;
import club.hm.matrix.auth.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        return Mono.justOrEmpty(authentication)
                .map(auth -> {
                    return Optional.ofNullable(auth.getCredentials())
                            .map(String::valueOf)
                            .map(this::convert)
                            .orElse(authentication);
                });
    }

    public Authentication convert(String token) {
        try{
            var claimsJws = jwtTokenProvider.decryptToken(token);
            var subject = claimsJws.getPayload().getSubject();
            var principal = tokenToPrincipal(subject);
            if (principal == null)
                return new AnonymousAuthentication();

            return new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        }catch (Exception ex){
            return new AnonymousAuthentication();
        }
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