package osins.matrix.auth.oauth2.server.handler.response;

import osins.matrix.auth.oauth2.server.enums.ResponseType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface IResponseTypeHandler<R> {
    ResponseType responseType();
    Mono<R> handle(ServerHttpRequest request);
}
