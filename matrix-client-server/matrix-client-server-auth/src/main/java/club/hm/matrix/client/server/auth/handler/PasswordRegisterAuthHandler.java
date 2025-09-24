package club.hm.matrix.client.server.auth.handler;

import club.hm.matrix.auth.grpc.LoadUserByUsernameRequest;
import club.hm.matrix.client.server.auth.vo.RegisterAvailableResult;
import club.hm.matrix.client.server.common.enums.RegisterAuthType;
import club.hm.matrix.client.server.auth.vo.TokenResponse;
import club.hm.matrix.client.server.auth.vo.RegisterRequest;
import club.hm.matrix.auth.grpc.CreateUserRequest;
import club.hm.matrix.auth.api.service.TokenService;
import club.hm.matrix.auth.grpc.consumer.generated.UserAuthorityGrpcClient;
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
    public Mono<RegisterAvailableResult> available(String username) {
        return userAuthorityClient.loadUserByUsername(LoadUserByUsernameRequest.newBuilder()
                        .setUsername(username)
                        .build())
                .handle((response, sink) -> {
                    // 检查gRPC响应是否包含错误
                    if (response.hasError()) {
                        // 如果有错误，记录日志并默认用户名可用
                        log.warn("Error checking username availability: {}", response.getError());
                        sink.next(RegisterAvailableResult.builder().available(false).build());
                    } else {
                        // 如果没有错误，检查用户是否存在
                        boolean userExists = response.hasUser() && username.equals(response.getUser().getUsername());
                        sink.next(RegisterAvailableResult.builder().available(!userExists).build());
                    }
                    sink.complete();
                });
    }

    @Override
    public Mono<TokenResponse> handle(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            return Mono.error(new IllegalArgumentException("密码不能为空"));
        }

        log.debug("Password auth for username={}/{}", request.getUsername(), request.getPassword());

        return userAuthorityClient.createUser(CreateUserRequest.newBuilder()
                        .setUsername(request.getUsername())
                        .setPassword(passwordEncoderService.encode(request.getPassword()))
                        .build())
                .map(response -> {
                    return Optional.of(response.getUser())
                            .map(tokenService::generateToken).orElseThrow(() -> new RuntimeException("用户名不能为空"));
                });
    }
}