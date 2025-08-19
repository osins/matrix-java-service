package club.hm.matrix.auth.client.handler;

import club.hm.matrix.auth.client.vo.TokenResponse;
import club.hm.matrix.auth.grpc.User;
import club.hm.matrix.auth.security.domain.CustomPrincipal;
import club.hm.matrix.auth.security.domain.JwtSetting;
import club.hm.matrix.auth.security.enums.JWTokenType;
import club.hm.matrix.auth.security.jwt.JwtTokenProvider;
import club.hm.matrix.auth.api.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService<TokenResponse> {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public TokenResponse generateToken(User user) {
        var userId = "@%s:matrix.org".formatted(user.getUsername());
        var jwtSettings = JwtSetting.create().setExpiration(3600)
                .setJti(UUID.randomUUID().toString())
                .setKeyId("matrix.org.v1")
                .setType(JWTokenType.ACCESS_TOKEN)
                .setSubject(CustomPrincipal.builder()
                        .username(user.getUsername())
                        .userId(user.getId())
                        .authorities(user.getRolesList().stream().map(role -> new SimpleGrantedAuthority(role.getCode())).toList())
                        .build());

        var expiration = 3600;
        var accessToken = jwtTokenProvider.generateToken(jwtSettings.setType(JWTokenType.ACCESS_TOKEN).setExpiration(expiration));
        var refreshToken = jwtTokenProvider.generateToken(jwtSettings.setType(JWTokenType.REFRESH_TOKEN).setExpiration(expiration * 3));

        return TokenResponse.builder()
                .userId(userId)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresInMs(expiration * 1000)
                .deviceId(UUID.randomUUID().toString())
                .homeServer("matrix.org")
                .build();
    }
}
