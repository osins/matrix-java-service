package io.osins.matrix.client.server.api.ws;

import io.osins.matrix.client.server.api.annotation.MessageMapping;
import io.osins.matrix.client.server.api.annotation.WsHandler;
import io.osins.matrix.client.server.common.domain.MatrixMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Sinks;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@WsHandler
public class RoomHandler {
    private final ConcurrentMap<String, Sinks.Many<String>> roomSinks = new ConcurrentHashMap<>();

    @MessageMapping("/_matrix/client/r0/events")
    public void events(MatrixMessageEvent payload, Sinks.Many<MatrixMessageEvent> sink, WebSocketSession session) {
      log.info("events: {}", payload);
        payload.addExtra("sinkType", "room");
        sink.tryEmitNext(payload);
//      sink.tryEmitComplete();
    }
}
