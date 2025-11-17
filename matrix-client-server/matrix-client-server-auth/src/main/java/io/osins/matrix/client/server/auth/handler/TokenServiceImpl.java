package io.osins.matrix.client.server.auth.handler;

import io.osins.matrix.client.server.auth.vo.TokenResponse;
import io.osins.matrix.auth.grpc.User;
import io.osins.matrix.auth.security.domain.CustomPrincipal;
import io.osins.matrix.auth.security.domain.JwtSetting;
import io.osins.matrix.auth.security.enums.JWTokenType;
import io.osins.matrix.auth.security.jwt.JwtTokenProvider;
import io.osins.matrix.auth.api.service.TokenService;
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
                .accessToken(response.accessToken)
                .refreshToken(response.refreshToken)
                .expiresInMs(response.expiresInMs)
                .deviceId(UUID.randomUUID().toString())
                .build();
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        if(!jwtTokenProvider.validateToken(refreshToken)){
            return TokenResponse.builder()
                    .errcode("M_UNKNOWN_TOKEN")
                    .error("Soft logged out")
                    .softLogout(true)
                    .build();
        }

        var token = jwtTokenProvider.generateToken(refreshToken);

        return TokenResponse.builder()
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .expiresInMs(token.getExpiresInMs())
                .deviceId(UUID.randomUUID().toString())
                .homeServer("matrix.org")
                .build();
    }
}
