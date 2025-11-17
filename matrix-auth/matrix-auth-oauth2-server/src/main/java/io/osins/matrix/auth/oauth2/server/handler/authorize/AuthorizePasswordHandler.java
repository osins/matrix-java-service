package io.osins.matrix.auth.oauth2.server.handler.authorize;

import io.osins.matrix.auth.api.service.TokenService;
import io.osins.matrix.auth.grpc.LoadUserByUsernameRequest;
import io.osins.matrix.auth.grpc.UserResponse;
import io.osins.matrix.auth.grpc.consumer.generated.UserAuthorityGrpcClient;
import io.osins.matrix.auth.oauth2.server.enums.GrantType;
import io.osins.matrix.auth.oauth2.server.vo.TokenResponse;
import io.osins.matrix.auth.security.service.PasswordEncoderService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizePasswordHandler implements IAuthorizeHandler<TokenResponse> {
    private final UserAuthorityGrpcClient userAuthorityGrpcClient;
    private final PasswordEncoderService passwordEncoderService;
    private final TokenService<TokenResponse> tokenService;

    @Override
    public GrantType grantType() {
        return GrantType.PASSWORD;
    }

    @Override
    public Mono<TokenResponse> handle(@NonNull ServerHttpRequest request) {
        var params = request.getQueryParams();
        if (!params.containsKey("username")) {
            return Mono.error(new RuntimeException("用户名不能为空"));
        }

        if (!params.containsKey("password")) {
            return Mono.error(new RuntimeException("密码不能为空"));
        }

        var username = params.getFirst("username");
        var password = params.getFirst("password");

        return userAuthorityGrpcClient.loadUserByUsername(LoadUserByUsernameRequest.newBuilder()
                .setUsername(username)
                .build())
                .map(UserResponse::getUser)
                .mapNotNull(user -> {
                    if (passwordEncoderService.matches(password, user.getPassword())) {
                        log.debug("用户 {} 密码验证成功!", user.getUsername());
                        return tokenService.generateToken(user);
                    }

                    log.debug("用户 {} 密码验证失败!", user.getUsername());
                    return null;
                });
    }
}
