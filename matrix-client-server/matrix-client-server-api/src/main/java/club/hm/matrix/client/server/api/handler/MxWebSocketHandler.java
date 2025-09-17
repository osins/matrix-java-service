package club.hm.matrix.client.server.api.handler;

import club.hm.homemart.club.shared.common.uitls.JSON;
import club.hm.homemart.club.shared.common.uitls.Strings;
import club.hm.matrix.client.server.api.annotation.MessageMapping;
import club.hm.matrix.client.server.api.annotation.WsHandler;
import club.hm.matrix.client.server.common.domain.MatrixMessageEvent;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MxWebSocketHandler {
    private final HandlerMapping mapping;

    // 用于广播事件到所有订阅用户
    private static final Sinks.Many<MatrixMessageEvent> broadcastMessageSink = Sinks.many().multicast().onBackpressureBuffer();
    private final Map<String, Sinks.Many<MatrixMessageEvent>> roomSinks = new ConcurrentHashMap<>();

    public MxWebSocketHandler(ApplicationContext context) {
        var handlers = new HashMap<String, WebSocketHandler>();
        var beanNames = context.getBeanNamesForAnnotation(WsHandler.class);
        for (var beanName : beanNames) {
            var controller = context.getBean(beanName);
            for (Method method : controller.getClass().getMethods()) {
                if (method.isAnnotationPresent(MessageMapping.class)) {
                    var path = method.getAnnotation(MessageMapping.class).value();
                    handlers.put(path, new WebSocketHandler() {
                        @NonNull
                        @Override
                        public List<String> getSubProtocols() {
                            return WebSocketHandler.super.getSubProtocols();
                        }

                        @NonNull
                        @Override
                        public Mono<Void> handle(@NonNull WebSocketSession session) {
                            log.info("session[{}]: {}", session.getId(), session.getHandshakeInfo());

                            var clientSink = Sinks.many().replay().<MatrixMessageEvent>all();
                            var clientSinkFlux = clientSink.asFlux();

                            var sendMono = session.send(clientSinkFlux
                                    .mergeWith(broadcastMessageSink.asFlux()
                                            .map(msg -> {
                                                log.info("broadcast sink send message: {}", msg);
                                                return msg;
                                            }))
                                    .map(payload -> {
                                        log.info("send message to client: {}", payload);
                                        return JSON.toJSONString(payload);
                                    })
                                    .map(session::textMessage)
                            );

                            var receiveMono = session.receive()
                                    .map(WebSocketMessage::getPayloadAsText)
                                    .map(payload -> JSON.toOptional(payload, MatrixMessageEvent.class))
                                    .filter(Optional::isPresent)
                                    .map(Optional::get)
                                    .doOnNext(payload -> {
                                        log.info("server receive: {}", payload);
                                        payload.addExtra("sinkType", "broadcast");
                                        broadcastMessageSink.tryEmitNext(payload);
                                        getRoomSink(payload.getRoomId(), clientSink).ifPresent(sink -> {
                                            invoke(session, payload, sink, method, controller);
                                        });
                                    }).then();

                            // 并行执行发送和接收
                            return Mono.when(sendMono, receiveMono);
                        }
                    });
                }
            }
        }

        mapping = new SimpleUrlHandlerMapping(handlers, -1);
    }

    private static void invoke(WebSocketSession session, MatrixMessageEvent payload, Sinks.Many<MatrixMessageEvent> sink, Method method, Object controller) {
        try {
            method.invoke(controller, payload, sink, session);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("invoke error: {}", e.getMessage(), e);
        }
    }

    private Optional<Sinks.Many<MatrixMessageEvent>> getRoomSink(String roomId, Sinks.Many<MatrixMessageEvent> clientSink) {
        log.info("getRoomSink, room id: {}", roomId);

        if (Strings.isNoneNullOrEmpty(roomId)) {
            if (roomSinks.containsKey(roomId)) {
                return Optional.of(roomSinks.get(roomId));
            } else {
                var roomSink = Sinks.many().multicast().<MatrixMessageEvent>onBackpressureBuffer();
                roomSink.asFlux()
                        .map(msg -> {
                            log.info("room sink send message: {}", msg);
                            return msg;
                        }).subscribe(clientSink::tryEmitNext);
                roomSinks.put(roomId, roomSink);
                return Optional.of(roomSink);
            }
        }

        log.warn("roomId is null");

        return Optional.empty();
    }

    public HandlerMapping getHandlerMapping() {
        return mapping;
    }
}