package club.hm.matrix.auth.security.api.login.handler;

import club.hm.matrix.auth.api.enums.LoginAuthType;
import club.hm.matrix.auth.api.token.TokenResponse;
import club.hm.matrix.auth.grpc.LoadUserByUsernameRequest;
import club.hm.matrix.auth.grpc.UserResponse;
import club.hm.matrix.auth.grpc.api.service.UserAuthorityGrpc;
import club.hm.matrix.auth.login.handler.LoginAuthHandler;
import club.hm.matrix.auth.login.vo.LoginRequest;
import club.hm.matrix.auth.security.service.TokenService;
import club.hm.matrix.auth.security.service.UserPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordLoginAuthHandler implements LoginAuthHandler {
    private final UserAuthorityGrpc userAuthorityGrpc;
    private final UserPasswordService userPasswordService;
    private final TokenService tokenService;

    @Override
    public LoginAuthType getAuthType() {
        return LoginAuthType.PASSWORD;
    }

    @Override
    public Mono<TokenResponse> handle(LoginRequest request) {
        return userAuthorityGrpc.loadUserByUsername(LoadUserByUsernameRequest.newBuilder()
                        .setUsername(request.getIdentifier().getUser())
                        .build())
                .map(UserResponse::getUser)
                .flatMap(user -> {
                    if (userPasswordService.matches(request.getPassword(), user.getPassword())) {
                        return Mono.just(tokenService.generateToken(user));
                    } else {
                        return Mono.error(new RuntimeException("密码错误"));
                    }
                });
    }
}
