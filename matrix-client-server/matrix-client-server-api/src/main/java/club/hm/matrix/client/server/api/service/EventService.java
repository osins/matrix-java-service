package club.hm.matrix.client.server.api.service;

import club.hm.matrix.client.server.common.domain.MatrixEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Service
public class EventService<T> {

    private final Sinks.Many<MatrixEvent<T>> sink = Sinks.many().multicast().onBackpressureBuffer();

    public void pushEvent(MatrixEvent<T> event) {
        sink.tryEmitNext(event);
    }

    public Flux<MatrixEvent<T>> subscribe(String roomId) {
        return sink.asFlux()
                .filter(event -> event.getRoomId().equals(roomId));
    }
}