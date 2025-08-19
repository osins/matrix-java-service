package club.hm.matrix.auth.api.service;

import reactor.core.publisher.Mono;

public interface LoginService<T, R> {
    Mono<R> login(T request);
}
