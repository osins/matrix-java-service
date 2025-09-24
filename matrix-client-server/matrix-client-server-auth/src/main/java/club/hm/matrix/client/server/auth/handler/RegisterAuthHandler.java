package club.hm.matrix.client.server.auth.handler;


import club.hm.matrix.client.server.auth.vo.RegisterAvailableResult;
import club.hm.matrix.client.server.common.enums.RegisterAuthType;
import club.hm.matrix.client.server.auth.vo.TokenResponse;
import club.hm.matrix.client.server.auth.vo.RegisterRequest;
import reactor.core.publisher.Mono;

public interface RegisterAuthHandler {
    RegisterAuthType getAuthType();

    Mono<RegisterAvailableResult> available(String username);

    Mono<TokenResponse> handle(RegisterRequest request);
}