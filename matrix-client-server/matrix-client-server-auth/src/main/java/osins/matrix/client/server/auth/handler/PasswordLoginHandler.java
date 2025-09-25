package osins.matrix.client.server.auth.handler;

import osins.matrix.client.server.common.enums.LoginType;
import osins.matrix.client.server.auth.vo.TokenResponse;
import osins.matrix.client.server.auth.vo.LoginRequest;
import osins.matrix.auth.grpc.LoadUserByUsernameRequest;
import osins.matrix.auth.grpc.UserResponse;
import osins.matrix.auth.api.service.TokenService;
import osins.matrix.auth.grpc.consumer.generated.UserAuthorityGrpcClient;
import osins.matrix.auth.security.service.PasswordEncoderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordLoginHandler implements LoginHandler {
    private final UserAuthorityGrpcClient userAuthorityClient;
    private final PasswordEncoderService passwordEncoderService;
    private final TokenService<TokenResponse> tokenService;

    @Override
    public LoginType getType() {
        return LoginType.PASSWORD;
    }

    @Override
    public Mono<TokenResponse> handle(LoginRequest request) {
        return userAuthorityClient.loadUserByUsername(LoadUserByUsernameRequest.newBuilder().setUsername(request.getIdentifier().getUser()).build())
                .map(UserResponse::getUser)
                .flatMap(user -> {
                    if (passwordEncoderService.matches(request.getPassword(), user.getPassword())) {
                        return Mono.just(tokenService.generateToken(user));
                    } else {
                        log.debug("密码错误: {}/{}", request.getIdentifier().getUser(), request.getPassword());
                        return Mono.error(new RuntimeException("密码错误"));
                    }
                });
    }
}
