package io.osins.matrix.auth.security.filter;

import io.osins.shared.common.uitls.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.util.Optional;
import java.util.UUID;

import static io.osins.shared.common.log.MDCContext.REQUEST_ID_KEY;

@Slf4j
@Component
public class RequestIdUtils {
    public static String getRequestId(ServerWebExchange exchange) {
        return Optional.ofNullable(exchange.getAttributes().get(REQUEST_ID_KEY))
                .map(Object::toString)
                .filter(Strings::isNoneNullOrEmpty)
                .orElseGet(()->{
                    var newRequestId = UUID.randomUUID().toString();
                    exchange.getAttributes().put(REQUEST_ID_KEY, newRequestId);
                    exchange.getResponse().getHeaders().add(REQUEST_ID_KEY, newRequestId);

                    return newRequestId;
                });
    }
}
