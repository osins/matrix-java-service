package club.hm.matrix.auth.login.handler;

import club.hm.matrix.auth.api.enums.LoginAuthType;
import club.hm.matrix.auth.api.token.TokenResponse;
import club.hm.matrix.auth.login.vo.LoginRequest;
import reactor.core.publisher.Mono;

public interface LoginAuthHandler {
    LoginAuthType getAuthType();
    Mono<TokenResponse> handle(LoginRequest request);
}
