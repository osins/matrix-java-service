package club.hm.matrix.shared.tracing;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class Tracing {
    private final Tracer tracer;
    private final ObservationRegistry registry;

    public <T> Mono<T> observe(String name, String path, String value, Supplier<Mono<T>> supplier) {
        return Observation.createNotStarted(name, registry)
                .lowCardinalityKeyValue(path, value)
                .observe((() -> supplier.get().doOnNext(t -> {
                    log.debug("success: {}", t);
                }).doOnError(throwable -> {
                    log.error("error: {}", throwable.getMessage());
                }).doFinally(signalType -> {
                    log.debug("finally: {}", signalType);
                })));
    }


    public <T> Mono<T> span(String name, Supplier<Mono<T>> supplier) {
        return span(name, null, supplier);
    }

    public <T> Mono<T> span(String name, Consumer<Span> consumer, Supplier<Mono<T>> supplier) {
        return Mono.defer(() -> {
            var span = tracer.nextSpan().name(name).start();
            if (consumer != null)
                consumer.accept(span);

            try (Tracer.SpanInScope ignored = tracer.withSpan(span)) {
                return supplier.get();
            } finally {
                span.end();
            }
        });
    }

    public Spinning spinning(Observation.Context context) {
        return Spinning.builder(context);
    }
}
