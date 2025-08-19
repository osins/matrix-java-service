package club.hm.matrix.auth.oauth2.server.controller.test;

import club.hm.matrix.auth.oauth2.server.MatrixOauth2ServerApplication;
import club.hm.matrix.auth.oauth2.server.vo.OAuth2CodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringJUnitConfig@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorizeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testAuthorize_withResponseType() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/authorize/oauth2/v1/authorize")
                        .queryParam("response_type", "code")
                        .queryParam("state", RandomStringUtils.random(8, "0123456789"))
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data")
                .value(OAuth2CodeResponse.class, response->{
                    Assert.notNull(response.getCode(), "返回的 code 不应该为空");
                    System.out.println("返回的 code = " + response.getCode());

                    testAuthorize_withGrantType(response.getCode(), response.getState());
                }); // 验证返回结果存在
    }

    @Test
    void testAuthorize_withGrantType(String code, String state) {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/authorize/oauth2/v1/authorize")
                        .queryParam("grant_type", "password")
                        .queryParam("client_id", "client_id")
                        .queryParam("code", code)
                        .queryParam("state", state)
                        .queryParam("username", "richar2")
                        .queryParam("password", "pp2w1TX1KNHxMvUw1Dkg")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data").isNotEmpty();
    }

    @Test
    void testAuthorize_errorScenario() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/authorize/oauth2/v1/authorize")
                        .queryParam("grant_type", "invalid")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError(); // 或根据你的服务返回定义调整
    }
}