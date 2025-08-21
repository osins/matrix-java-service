package club.hm.matrix.auth.oauth2.server.handler.authorize;

import club.hm.matrix.auth.api.service.TokenService;
import club.hm.matrix.auth.grpc.LoadUserByUsernameRequest;
import club.hm.matrix.auth.grpc.UserResponse;
import club.hm.matrix.auth.grpc.api.service.UserAuthorityGrpc;
import club.hm.matrix.auth.oauth2.server.enums.GrantType;
import club.hm.matrix.auth.oauth2.server.service.SmsLoginCodeService;
import club.hm.matrix.auth.oauth2.server.vo.TokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizeSmsCodeHandler implements IAuthorizeHandler<TokenResponse> {
    private final UserAuthorityGrpc userAuthorityGrpc;
    private final SmsLoginCodeService smsLoginCodeService;
    private final TokenService<TokenResponse> tokenService;

    @Override
    public GrantType grantType() {
        return GrantType.SMS_CODE;
    }

    @Override
    public Mono<TokenResponse> handle(ServerHttpRequest request) {
        var params = request.getQueryParams();
        if (!params.containsKey("username")) {
            return Mono.error(new RuntimeException("用户名不能为空"));
        }

        if (!params.containsKey("code")) {
            return Mono.error(new RuntimeException("密码不能为空"));
        }

        var username = params.getFirst("username");
        var code = params.getFirst("code");

        return userAuthorityGrpc.loadUserByUsername(LoadUserByUsernameRequest.newBuilder()
                        .setUsername(username)
                        .build())
                .map(UserResponse::getUser)
                .filter(user -> user.getUsername().equals(username))
                .flatMap(user -> smsLoginCodeService.validateAndConsume(user.getUsername(), code)
                        .filter(valid -> valid)
                        .thenReturn(tokenService.generateToken(user)));
    }
}
