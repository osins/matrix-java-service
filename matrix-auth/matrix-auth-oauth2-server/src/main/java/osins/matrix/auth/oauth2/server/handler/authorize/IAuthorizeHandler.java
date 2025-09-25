package osins.matrix.auth.oauth2.server.handler.authorize;

import osins.matrix.auth.oauth2.server.enums.GrantType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface IAuthorizeHandler<R> {
    GrantType grantType();
    Mono<R> handle(ServerHttpRequest request);
}
