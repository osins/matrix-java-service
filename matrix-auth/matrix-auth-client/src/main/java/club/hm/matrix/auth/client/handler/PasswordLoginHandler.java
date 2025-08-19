package club.hm.matrix.auth.client.handler;

import club.hm.matrix.auth.client.enums.LoginType;
import club.hm.matrix.auth.client.vo.TokenResponse;
import club.hm.matrix.auth.client.vo.LoginRequest;
import club.hm.matrix.auth.grpc.LoadUserByUsernameRequest;
import club.hm.matrix.auth.grpc.UserResponse;
import club.hm.matrix.auth.grpc.api.service.UserAuthorityGrpc;
import club.hm.matrix.auth.api.service.TokenService;
import club.hm.matrix.auth.security.service.UserPasswordEncoderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordLoginHandler implements LoginHandler {
    private final UserAuthorityGrpc userAuthorityGrpc;
    private final UserPasswordEncoderService userPasswordEncoderService;
    private final TokenService<TokenResponse> tokenService;

    @Override
    public LoginType getType() {
        return LoginType.PASSWORD;
    }

    @Override
    public Mono<TokenResponse> handle(LoginRequest request) {
        return userAuthorityGrpc.loadUserByUsername(LoadUserByUsernameRequest.newBuilder()
                        .setUsername(request.getIdentifier().getUser())
                        .build())
                .map(UserResponse::getUser)
                .flatMap(user -> {
                    if (userPasswordEncoderService.matches(request.getPassword(), user.getPassword())) {
                        return Mono.just(tokenService.generateToken(user));
                    } else {
                        return Mono.error(new RuntimeException("密码错误"));
                    }
                });
    }
}
