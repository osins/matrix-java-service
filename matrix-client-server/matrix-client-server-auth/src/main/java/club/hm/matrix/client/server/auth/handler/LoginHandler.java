package club.hm.matrix.client.server.auth.handler;

import club.hm.matrix.client.server.common.enums.LoginType;
import club.hm.matrix.client.server.auth.vo.TokenResponse;
import club.hm.matrix.client.server.auth.vo.LoginRequest;
import reactor.core.publisher.Mono;

public interface LoginHandler {
    LoginType getType();
    Mono<TokenResponse> handle(LoginRequest request);
}
