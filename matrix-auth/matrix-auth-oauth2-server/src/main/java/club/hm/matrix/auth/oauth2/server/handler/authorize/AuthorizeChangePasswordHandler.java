package club.hm.matrix.auth.oauth2.server.handler.authorize;

import club.hm.homemart.club.shared.common.uitls.Result;
import club.hm.matrix.auth.grpc.ChangePasswordByUsernameRequest;
import club.hm.matrix.auth.grpc.api.service.UserAuthorityGrpc;
import club.hm.matrix.auth.oauth2.server.enums.GrantType;
import club.hm.matrix.auth.oauth2.server.service.SmsLoginCodeService;
import club.hm.matrix.auth.security.service.PasswordEncoderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorizeChangePasswordHandler implements IAuthorizeHandler<Result<Void>> {
    private final UserAuthorityGrpc userAuthorityGrpc;
    private final SmsLoginCodeService smsLoginCodeService;
    private final PasswordEncoderService passwordEncoderService;

    @Override
    public GrantType grantType() {
        return GrantType.CHANGE_PASSWORD;
    }

    @Override
    public Mono<Result<Void>> handle(ServerHttpRequest request) {
        var mobile = request.getQueryParams().getFirst("mobile");
        var code = request.getQueryParams().getFirst("code");
        var password = request.getQueryParams().getFirst("password");

        return smsLoginCodeService.validateAndConsume(mobile, code)
                .flatMap(result -> {
                    if(!result)
                        return Mono.error(new RuntimeException("验证码错误"));

                    return userAuthorityGrpc.changePasswordByUsername(ChangePasswordByUsernameRequest.newBuilder()
                                    .setUsername(mobile)
                                    .setNewPassword(passwordEncoderService.encode(password))
                                    .build())
                            .thenReturn(Result.success());
                });
    }
}
