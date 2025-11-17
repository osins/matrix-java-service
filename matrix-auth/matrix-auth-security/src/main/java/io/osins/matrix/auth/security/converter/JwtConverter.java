package io.osins.matrix.auth.security.converter;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Claims;
import org.springframework.security.oauth2.jwt.Jwt;
import java.time.Instant;
import java.util.HashMap;

public class JwtConverter {

    public static Jwt convertJwsToJwt(Jws<Claims> jws, String tokenValue) {
        var claims = jws.getPayload();
        var header = jws.getHeader();

        // 使用 Map.of() 构建headers (Java 9+)
        var headers = new HashMap<String, Object>();
        if (header.getAlgorithm() != null) {
            headers.put("alg", header.getAlgorithm());
        }

        if (header.getType() != null) {
            headers.put("typ", header.getType());
        }
        // 使用 Optional 风格处理时间 (更现代的写法)
        var issuedAt = claims.getIssuedAt() != null ?
                claims.getIssuedAt().toInstant() : Instant.now();
        var expiresAt = claims.getExpiration() != null ?
                claims.getExpiration().toInstant() : null;

        return new Jwt(tokenValue, issuedAt, expiresAt, headers, claims);
    }
}