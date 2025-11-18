package io.osins.matrix.auth.security.handler;

import io.osins.shared.common.log.MDCContext;
import io.osins.matrix.auth.security.filter.RequestIdUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler implements WebExceptionHandler {
    @Override
    public @NonNull Mono<Void> handle(@NonNull ServerWebExchange exchange, @NonNull Throwable ex) {
        return MDCContext.withMDC(Mono.deferContextual(ctx -> {
            // 直接从 exchange attributes 获取 requestId
            var requestId = RequestIdUtils.getRequestId(exchange);
            if (requestId == null) {
                requestId = "N/A";
            }

            var status = HttpStatus.INTERNAL_SERVER_ERROR;

            log.error("Exception[{}]: {}", requestId, ex.getMessage(), ex);

            var message = ex.getMessage();

            // 可以根据不同异常类型映射不同 HTTP 状态
            if (ex instanceof IllegalArgumentException) {
                status = HttpStatus.BAD_REQUEST;
            } else if (ex instanceof DuplicateKeyException) {
                status = HttpStatus.CONFLICT;
            }

            var errorResponse = new HashMap<String, Object>();
            errorResponse.put("timestamp", Instant.now().toEpochMilli());
            errorResponse.put("path", exchange.getRequest().getPath().value());
            errorResponse.put("status", status.value());
            errorResponse.put("error", message);
            errorResponse.put("requestId", ctx.getOrDefault(MDCContext.REQUEST_ID_KEY, "N/A"));

            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

            try {
                var bytes = new com.fasterxml.jackson.databind.ObjectMapper()
                        .writeValueAsBytes(errorResponse);

                return exchange.getResponse().writeWith(Mono.just(
                        exchange.getResponse().bufferFactory().wrap(bytes)
                ));
            } catch (Exception e) {
                log.error("Error writing authentication error response: {}", e.getMessage(), e);

                return exchange.getResponse().writeWith(Mono.just(
                        exchange.getResponse().bufferFactory().wrap(e.getMessage().getBytes(StandardCharsets.UTF_8))
                ));
            }
        }));
    }
}
