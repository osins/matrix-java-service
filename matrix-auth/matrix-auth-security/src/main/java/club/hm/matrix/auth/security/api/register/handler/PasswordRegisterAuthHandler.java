package club.hm.matrix.auth.security.api.register.handler;

import club.hm.matrix.auth.api.enums.RegisterAuthType;
import club.hm.matrix.auth.grpc.CreateUserRequest;
import club.hm.matrix.auth.grpc.User;
import club.hm.matrix.auth.grpc.api.service.UserAuthorityGrpc;
import club.hm.matrix.auth.register.handler.RegisterAuthHandler;
import club.hm.matrix.auth.register.vo.RegisterRequest;
import club.hm.matrix.auth.api.token.TokenResponse;
import club.hm.matrix.auth.security.service.TokenService;
import club.hm.matrix.auth.security.service.UserPasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component()
@RequiredArgsConstructor
public class PasswordRegisterAuthHandler implements RegisterAuthHandler {
    private final UserAuthorityGrpc userAuthorityGrpc;
    private final UserPasswordService userPasswordService;
    private final TokenService tokenService;

    @Override
    public RegisterAuthType getAuthType() {
        return RegisterAuthType.PASSWORD;
    }

    @Override
    public Mono<TokenResponse> handle(RegisterRequest request) {
        if (request.password() == null || request.password().isEmpty()) {
            return Mono.error(new IllegalArgumentException("密码不能为空"));
        }
        log.info("Password auth for username={}", request.username());

        return userAuthorityGrpc.createUser(CreateUserRequest.newBuilder()
                        .setUser(User.newBuilder()
                                .setUsername(request.username())
                                .setPassword(userPasswordService.encode(request.password()))
                                .build())
                        .build())
                .map(response -> {
                    return Optional.of(response.getUser())
                            .map(tokenService::generateToken).orElseThrow(() -> new RuntimeException("用户名不能为空"));
                });
    }
}