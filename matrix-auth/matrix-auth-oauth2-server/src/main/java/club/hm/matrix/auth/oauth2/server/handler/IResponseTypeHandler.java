package club.hm.matrix.auth.oauth2.server.handler;

import club.hm.matrix.auth.oauth2.server.enums.ResponseType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Mono;

public interface IResponseTypeHandler<R> {
    ResponseType responseType();
    Mono<R> handle(ServerHttpRequest request);
}
