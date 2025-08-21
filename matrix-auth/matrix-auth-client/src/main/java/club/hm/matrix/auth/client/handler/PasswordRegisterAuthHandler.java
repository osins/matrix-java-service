package club.hm.matrix.auth.client.handler;

import club.hm.matrix.auth.client.enums.RegisterAuthType;
import club.hm.matrix.auth.client.vo.TokenResponse;
import club.hm.matrix.auth.client.vo.RegisterRequest;
import club.hm.matrix.auth.grpc.CreateUserRequest;
import club.hm.matrix.auth.grpc.User;
import club.hm.matrix.auth.api.service.TokenService;
import club.hm.matrix.auth.grpc.consumer.service.impl.UserAuthorityGrpcClient;
import club.hm.matrix.auth.security.service.PasswordEncoderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component()
@RequiredArgsConstructor
public class PasswordRegisterAuthHandler implements RegisterAuthHandler {
    private final UserAuthorityGrpcClient userAuthorityClient;
    private final PasswordEncoderService passwordEncoderService;
    private final TokenService<TokenResponse> tokenService;

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

        return userAuthorityClient.createUser(CreateUserRequest.newBuilder()
                        .setUsername(request.username())
                        .setPassword(passwordEncoderService.encode(request.password()))
                        .build())
                .map(response -> {
                    return Optional.of(response.getUser())
                            .map(tokenService::generateToken).orElseThrow(() -> new RuntimeException("用户名不能为空"));
                });
    }
}