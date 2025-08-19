package club.hm.matrix.auth.oauth2.server.handler;

import club.hm.matrix.auth.oauth2.server.enums.ResponseType;
import club.hm.matrix.auth.oauth2.server.service.AuthorizationCodeService;
import club.hm.matrix.auth.oauth2.server.vo.OAuth2CodeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseCodeHandler implements IResponseTypeHandler<OAuth2CodeResponse> {
    private final AuthorizationCodeService authorizationCodeService;

    @Override
    public ResponseType responseType() {
        return ResponseType.CODE;
    }

    @Override
    public Mono<OAuth2CodeResponse> handle(ServerHttpRequest request) {
        var state = request.getQueryParams().getFirst("state");
        if(state == null)
            return Mono.error(new RuntimeException("state不能为空"));
        return authorizationCodeService.generate(state)
                .map(code-> OAuth2CodeResponse.builder()
                        .code(code)
                        .state(state)
                        .clientId(request.getQueryParams().getFirst("client_id"))
                        .build());
    }
}
