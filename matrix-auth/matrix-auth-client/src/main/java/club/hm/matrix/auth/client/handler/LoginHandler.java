package club.hm.matrix.auth.client.handler;

import club.hm.matrix.auth.client.enums.LoginType;
import club.hm.matrix.auth.client.vo.TokenResponse;
import club.hm.matrix.auth.client.vo.LoginRequest;
import reactor.core.publisher.Mono;

public interface LoginHandler {
    LoginType getType();
    Mono<TokenResponse> handle(LoginRequest request);
}
