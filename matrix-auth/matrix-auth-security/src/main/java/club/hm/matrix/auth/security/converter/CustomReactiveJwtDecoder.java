package club.hm.matrix.auth.security.converter;

import club.hm.matrix.auth.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static club.hm.matrix.auth.security.converter.JwtConverter.convertJwsToJwt;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomReactiveJwtDecoder implements ReactiveJwtDecoder {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Jwt> decode(String token) throws JwtException {
        return Mono.defer(()->{
            var jwt = jwtTokenProvider.decryptToken(token);
            return Mono.just(convertJwsToJwt(jwt, token));
        });
    }
}
