package osins.matrix.client.server.auth.handler;


import osins.matrix.client.server.auth.vo.RegisterAvailableResult;
import osins.matrix.client.server.common.enums.RegisterAuthType;
import osins.matrix.client.server.auth.vo.TokenResponse;
import osins.matrix.client.server.auth.vo.RegisterRequest;
import reactor.core.publisher.Mono;

public interface RegisterAuthHandler {
    RegisterAuthType getAuthType();

    Mono<RegisterAvailableResult> available(String username);

    Mono<TokenResponse> handle(RegisterRequest request);
}