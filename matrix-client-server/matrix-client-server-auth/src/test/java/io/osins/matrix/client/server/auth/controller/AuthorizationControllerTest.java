package io.osins.matrix.client.server.auth.controller;


import io.osins.shared.common.uitls.Random;
import io.osins.matrix.client.server.auth.vo.LoginRequest;
import io.osins.matrix.client.server.common.enums.LoginType;
import io.osins.matrix.client.server.common.enums.RegisterAuthType;
import io.osins.matrix.client.server.auth.vo.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorizationControllerTest {

    @Autowired
    private WebTestClient webTestClient;


    @Test
    void testRegisterNewUser() {
        var registerRequest = RegisterRequest.builder()
                .username("integrationUser2" + Random.number(4))
                .password("123456")
                .auth(RegisterRequest.Auth.builder()
                        .type(RegisterAuthType.PASSWORD)
                        .build())
                .build();

        webTestClient.post()
                .uri("/_matrix/client/v3/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(registerRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    System.out.println(new String(response.getResponseBody()));
                })
                .jsonPath("$.user_id").isNotEmpty()
                .jsonPath("$.access_token").isNotEmpty()
                .jsonPath("$.device_id").isNotEmpty();

        var loginRequest = LoginRequest.builder()
                .type(LoginType.PASSWORD)
                .password(registerRequest.getPassword())
                .identifier(LoginRequest.Identifier.builder()
                        .user(registerRequest.getUsername())
                        .type("m.id.user")
                        .build())
                .initialDeviceDisplayName("localhost:8080: Chrome on Linux")
                .build();

        webTestClient.post()
                .uri("/_matrix/client/v3/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    System.out.println(new String(response.getResponseBody()));
                })
                .jsonPath("$.user_id").isNotEmpty()
                .jsonPath("$.access_token").isNotEmpty()
                .jsonPath("$.device_id").isNotEmpty();
    }

    @Test
    void testRegisterDuplicateUser() {
        var request = Map.of(
                "username", "integrationUser",
                "password", "password123"
        );

        // 第一次注册
        webTestClient.post()
                .uri("/_matrix/client/v3/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk();

        // 第二次注册同名用户，应该返回 400 + M_USER_IN_USE
        webTestClient.post()
                .uri("/_matrix/client/v3/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.errcode").isEqualTo("M_USER_IN_USE");
    }

}