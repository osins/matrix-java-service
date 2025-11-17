package io.osins.matrix.auth.oauth2.server.service;

import io.osins.matrix.auth.api.service.TokenService;
import io.osins.matrix.auth.grpc.User;
import io.osins.matrix.auth.oauth2.server.vo.TokenResponse;
import io.osins.matrix.auth.security.domain.CustomPrincipal;
import io.osins.matrix.auth.security.domain.JwtSetting;
import io.osins.matrix.auth.security.enums.JWTokenType;
import io.osins.matrix.auth.security.jwt.JwtTokenProvider;
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
        var jwtSettings = JwtSetting.create()
                .setJti(UUID.randomUUID().toString())
                .setType(JWTokenType.ACCESS_TOKEN)
                .setSubject(CustomPrincipal.builder()
                        .username(user.getUsername())
                        .userId(user.getId())
                        .authorities(user.getRolesList().stream().map(role -> new SimpleGrantedAuthority(role.getCode())).toList())
                        .build());

        var response = jwtTokenProvider.generateToken(jwtSettings);

        return TokenResponse.builder()
                .userId(userId)
                .accessToken(response.getAccessToken())
                .refreshToken(response.getRefreshToken())
                .expiresInMs(response.getExpiresInMs())
                .deviceId(UUID.randomUUID().toString())
                .homeServer("matrix.org")
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if(!jwtTokenProvider.validateToken(refreshToken)){
            return null;
        }

        var response = jwtTokenProvider.generateToken(refreshToken);

        return TokenResponse.builder()
                .accessToken(response.getAccessToken())
                .refreshToken(response.getRefreshToken())
                .expiresInMs(response.getExpiresInMs())
                .deviceId(UUID.randomUUID().toString())
                .deviceId(UUID.randomUUID().toString())
                .homeServer("matrix.org")
                .build();
    }
}
