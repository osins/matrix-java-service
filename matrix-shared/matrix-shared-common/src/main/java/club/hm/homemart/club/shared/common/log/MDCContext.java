package club.hm.homemart.club.shared.common.log;

import org.slf4j.MDC;
import reactor.core.publisher.Mono;

public class MDCContext {
    public static final String REQUEST_ID_KEY = "X-Request-ID";

    public static <T> Mono<T> withMDC(Mono<T> mono) {
        return Mono.deferContextual(ctx -> {
            var requestId = ctx.<String>getOrDefault(REQUEST_ID_KEY, null);
            if (requestId != null) {
                MDC.put(REQUEST_ID_KEY, requestId);
            }
            return mono.doFinally(signalType -> MDC.remove(REQUEST_ID_KEY));
        });
    }
}