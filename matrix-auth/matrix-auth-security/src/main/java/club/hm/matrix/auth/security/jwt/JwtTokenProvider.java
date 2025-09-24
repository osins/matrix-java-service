package club.hm.matrix.auth.security.jwt;

import club.hm.homemart.club.shared.common.Exception.CustomException;
import club.hm.matrix.auth.security.config.SecurityJwtConfig;
import club.hm.matrix.auth.security.domain.JwtSetting;
import club.hm.matrix.auth.security.domain.TokenResponse;
import club.hm.matrix.auth.security.enums.JWTokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(SecurityJwtConfig.class)
public class JwtTokenProvider {
    private static final String ALGORITHM = "HmacSHA512";

    private final SecurityJwtConfig config;
    private final ObjectMapper objectMapper;

    public static String generateSecretKey() throws NoSuchAlgorithmException {
        var keygen = KeyGenerator.getInstance(ALGORITHM);
        keygen.init(512);

        return Base64.getEncoder().encodeToString(keygen.generateKey().getEncoded());
    }


    private SecretKey getSecretKeySpec() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(config.getSecret()));
    }

    private ZoneId getZoneId() {
        try {
            return ZoneId.of(config.getZoneId());
        } catch (Exception e) {
            throw new CustomException(4937, "无效的时区ID: " + config.getZoneId());
        }
    }


    public TokenResponse generateToken(JwtSetting setting) {
        return TokenResponse.builder()
                .accessToken(generateToken(setting, JWTokenType.ACCESS_TOKEN))
                .refreshToken(generateToken(setting, JWTokenType.REFRESH_TOKEN))
                .expiresInMs(Long.valueOf(config.getExpiration() * 1000).intValue())
                .build();
    }

    public String generateToken(JwtSetting setting, JWTokenType type) {
        log.debug("generate token, current default time zone: {}", ZoneId.systemDefault());

        var zoneId = getZoneId();
        var now = ZonedDateTime.now(zoneId);
        var expiration = now.plusSeconds(type.isAccess() ? config.getExpiration() : config.getRefreshExpiration());

        var nowInstant = now.toInstant();
        var expInstant = expiration.toInstant();

        log.debug("生成 token 当前时间（{}）：{}", zoneId, now);
        log.debug("过期时间：{}", expiration);

        try {
            var subject = objectMapper.writeValueAsString(Optional.ofNullable(setting.getSubject())
                    .orElseThrow(() -> new CustomException("jwt token 缓存 subject 不能为空")));

            return Jwts.builder()
                    .header()
                    .keyId(config.getKeyId())
                    .and()
                    .subject(subject)
                    .signWith(getSecretKeySpec())
                    .notBefore(Date.from(nowInstant))
                    .issuedAt(Date.from(nowInstant))
                    .expiration(Date.from(expInstant))
                    .id(setting.getJti())
                    .compact();
        } catch (Exception ex) {
            log.error("生成 token 失败: {}, {}", setting, ex.getMessage(), ex);
            return null;
        }
    }

    public TokenResponse generateToken(String refreshToken) {
        return TokenResponse.builder()
                .accessToken(generateToken(refreshToken, JWTokenType.ACCESS_TOKEN))
                .refreshToken(generateToken(refreshToken, JWTokenType.REFRESH_TOKEN))
                .expiresInMs(Long.valueOf(config.getExpiration() * 1000).intValue())
                .build();
    }

    public String generateToken(String refreshToken, JWTokenType type) {
        log.debug("generate token by refresh token: {}", refreshToken);

        try {
            var claimsJws = decryptToken(refreshToken);
            var payload = claimsJws.getPayload();
            var subject = payload.getSubject();

            var zoneId = getZoneId();
            var now = ZonedDateTime.now(zoneId);
            var expiration = now.plusSeconds(type.isAccess() ? config.getExpiration() : config.getRefreshExpiration());

            var nowInstant = now.toInstant();
            var expInstant = expiration.toInstant();

            return Jwts.builder()
                    .header()
                    .keyId(config.getKeyId())
                    .and()
                    .subject(subject)
                    .signWith(getSecretKeySpec())
                    .notBefore(Date.from(nowInstant))
                    .issuedAt(Date.from(nowInstant))
                    .expiration(Date.from(expInstant))
                    .id(payload.getId())
                    .compact();
        } catch (Exception ex) {
            log.error("生成 token 失败: {}, {}, {}", config, refreshToken, ex.getMessage(), ex);
            return null;
        }
    }

    public Jws<Claims> decryptToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKeySpec())
                .build().parseSignedClaims(token);
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return decryptToken(token).getPayload().get("roles", List.class);
    }

    public boolean validateToken(String token) {
        try {
            // 解析并验证 Token
            decryptToken(token);

            return true;
        } catch (ExpiredJwtException e) {
            log.error("Token expired", e);
            return false;
        } catch (JwtException e) {
            log.error("Invalid Token", e);
            return false;
        }
    }
}