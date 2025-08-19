package club.hm.matrix.auth.oauth2.server.handler;


import club.hm.matrix.auth.oauth2.server.enums.GrantType;
import club.hm.matrix.auth.oauth2.server.vo.RegisterRequest;
import club.hm.matrix.auth.oauth2.server.vo.TokenResponse;
import reactor.core.publisher.Mono;

public interface RegisterHandler {
    GrantType getType();
    Mono<TokenResponse> handle(RegisterRequest request);
}