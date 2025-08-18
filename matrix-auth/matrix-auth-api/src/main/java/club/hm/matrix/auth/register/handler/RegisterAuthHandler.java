package club.hm.matrix.auth.register.handler;


import club.hm.matrix.auth.api.enums.RegisterAuthType;
import club.hm.matrix.auth.register.vo.RegisterRequest;
import club.hm.matrix.auth.api.token.TokenResponse;
import reactor.core.publisher.Mono;

public interface RegisterAuthHandler {
    RegisterAuthType getAuthType();
    Mono<TokenResponse> handle(RegisterRequest request);
}