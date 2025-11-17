package io.osins.matrix.client.server.api.controller;

import io.osins.matrix.client.server.api.MessageApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = MessageApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomHandlerTest {

    @LocalServerPort
    private int port; // 注入随机端口

    private String getServerUrl(String roomId) {
        return "http://localhost:" + port + "/rooms/" + roomId;
    }

    @Test
    void testSendAndSubscribeMessage() throws Exception {
        String roomId = "room1";
        String serverUrl = getServerUrl(roomId);

        var client = HttpClient.newHttpClient();
        var latch = new CountDownLatch(1);

        // 订阅消息
        var subscribeRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/subscribe"))
                .GET()
                .build();

        client.sendAsync(subscribeRequest, HttpResponse.BodyHandlers.ofLines())
                .thenAccept(response -> {
                    response.body().forEach(line -> {
                        System.out.println("Received: " + line);
                        latch.countDown();
                    });
                });

        Thread.sleep(500);

        // 发送消息
        var sendRequest = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/send?sender=Alice&content=HelloFromJUnit"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        var sendResponse = client.send(sendRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Send Response: " + sendResponse.statusCode() + " " + sendResponse.body());

        boolean received = latch.await(5, TimeUnit.SECONDS);
        assert received : "Message was not received in time";
    }
}
