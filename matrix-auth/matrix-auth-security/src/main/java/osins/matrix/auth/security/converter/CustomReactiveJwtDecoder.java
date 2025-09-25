package osins.matrix.auth.security.converter;

import osins.matrix.auth.security.exception.CustomAuthenticationException;
import osins.matrix.auth.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static osins.matrix.auth.security.converter.JwtConverter.convertJwsToJwt;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomReactiveJwtDecoder implements ReactiveJwtDecoder {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Mono<Jwt> decode(String token){
        return Mono.defer(()->{
            try{
                var jwt = jwtTokenProvider.decryptToken(token);
                return Mono.just(convertJwsToJwt(jwt, token));
            }catch (Exception ex){
                log.warn("decode error: {}, {}", token, ex.getMessage(), ex);
                return Mono.error(new CustomAuthenticationException("Invalid JWT token"));
            }
        });
    }
}
