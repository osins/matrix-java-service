package io.osins.matrix.client.server.api.controller;

import io.osins.shared.common.uitls.JSON;
import io.osins.matrix.client.server.api.MessageApplication;
import io.osins.matrix.client.server.common.domain.MatrixMessageEvent;
import io.osins.matrix.client.server.common.domain.RoomMessageContent;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

@Slf4j
@SpringBootTest(classes = MessageApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MatrixWebSocketHandlerTest {

    @LocalServerPort
    private int port;

    private final ReactorNettyWebSocketClient client = new ReactorNettyWebSocketClient();

    @Test
    public void testWebSocketEcho() throws InterruptedException {
        var latch = new CountDownLatch(1);
        var roomMessage = RoomMessageContent.builder()
                .msgtype("m.text")
                .body("hello")
                .build();
        var event = MatrixMessageEvent.builder()
                .type("m.room.message")
                .content(roomMessage)
                .build();
        client.execute(
                        URI.create("ws://localhost:" + port + "/_matrix/client/r0/events"),
                        session -> {
                            event.setEventId(UUID.randomUUID().toString());
                            event.setRoomId("!roomId:example.org");

                            // 接收消息
                            var receive = session.receive()
                                    .map(WebSocketMessage::getPayloadAsText)
                                    .map(payload -> {
                                        log.info("client received payload: {}", payload);
                                        return JSON.toOptional(payload, MatrixMessageEvent.class);
                                    })
                                    .filter(Optional::isPresent);

                            return session.send(Mono.just(event)
                                    .map(JSON::toJSONString)
                                    .map(session::textMessage))
                                    .thenMany(receive)
                                    .thenMany(receive.flatMap(payload -> {
                                        // 断言返回结果
                                        assert payload.isPresent();

                                        event.setRoomId(UUID.randomUUID().toString());

                                        return session.send(Mono.just(event)
                                                        .map(JSON::toJSONString)
                                                        .map(session::textMessage)
                                                        .delayElement(Duration.ofSeconds(3)));
                                    }))
                                    .then();
                        })
                .subscribe(); // 阻塞等待完成
// 阻塞等待测试结束
        latch.await();
    }
}