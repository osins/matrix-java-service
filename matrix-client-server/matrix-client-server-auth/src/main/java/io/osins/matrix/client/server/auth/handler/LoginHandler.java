package io.osins.matrix.client.server.auth.handler;

import io.osins.matrix.client.server.common.enums.LoginType;
import io.osins.matrix.client.server.auth.vo.TokenResponse;
import io.osins.matrix.client.server.auth.vo.LoginRequest;
import reactor.core.publisher.Mono;

public interface LoginHandler {
    LoginType getType();
    Mono<TokenResponse> handle(LoginRequest request);
}
