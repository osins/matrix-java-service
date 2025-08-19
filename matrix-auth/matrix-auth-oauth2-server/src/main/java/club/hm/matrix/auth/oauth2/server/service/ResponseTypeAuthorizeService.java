package club.hm.matrix.auth.oauth2.server.service;

import club.hm.homemart.club.shared.common.Result;
import club.hm.matrix.auth.oauth2.server.enums.ResponseType;
import club.hm.matrix.auth.oauth2.server.handler.IResponseTypeHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResponseTypeAuthorizeService {
    private final List<IResponseTypeHandler<?>> handlers;
    private final Map<ResponseType, IResponseTypeHandler<?>> maps = new EnumMap<>(ResponseType.class);

    @PostConstruct
    public void init() {
        handlers.forEach(handler -> maps.put(handler.responseType(), handler));
    }

    public Mono<?> get(ServerHttpRequest request){
        var type = ResponseType.of(request.getQueryParams().getFirst("response_type"));
        if(type.isUnknown())
            return Mono.just(Result.error("无效的授权模式"));

        return maps.get(type)
                .handle(request)
                .doOnNext(result -> log.info("request: {}, result: {}", request, result))
                .doOnError(e->log.debug("request: {}, error: {}", request, e.getMessage(), e));
    }
}
