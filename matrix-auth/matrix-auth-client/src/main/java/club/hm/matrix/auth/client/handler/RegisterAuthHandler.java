package club.hm.matrix.auth.client.handler;


import club.hm.matrix.auth.client.enums.RegisterAuthType;
import club.hm.matrix.auth.client.vo.TokenResponse;
import club.hm.matrix.auth.client.vo.RegisterRequest;
import reactor.core.publisher.Mono;

public interface RegisterAuthHandler {
    RegisterAuthType getAuthType();
    Mono<TokenResponse> handle(RegisterRequest request);
}