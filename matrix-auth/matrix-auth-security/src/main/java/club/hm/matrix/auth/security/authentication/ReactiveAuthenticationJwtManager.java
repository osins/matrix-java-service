package club.hm.matrix.auth.security.authentication;

import club.hm.matrix.auth.security.converter.AuthenticationJwtConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReactiveAuthenticationJwtManager implements ReactiveAuthenticationManager {
    private final AuthenticationJwtConverter bearerConverter;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws AuthenticationException {
        return Mono.justOrEmpty(authentication)
                .map(auth -> Optional.ofNullable(auth.getCredentials()).map(String::valueOf).orElseThrow(() -> new BadCredentialsException("Authentication failed")))
                .map(bearerConverter::convert);
    }
}