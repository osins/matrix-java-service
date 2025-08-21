package club.hm.matrix.auth.oauth2.server.controller.test;

import club.hm.homemart.club.shared.common.uitls.Random;
import club.hm.matrix.auth.oauth2.server.enums.GrantType;
import club.hm.matrix.auth.oauth2.server.service.SmsLoginCodeService;
import club.hm.matrix.auth.oauth2.server.vo.CodeResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.web.reactive.server.JsonPathAssertions;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;

@Slf4j
@SpringJUnitConfig
@AutoConfigureWebTestClient
@ComponentScan(basePackages = "club.hm.matrix")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthorizeControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private SmsLoginCodeService smsLoginCodeService;

    private JsonPathAssertions getCode() {
        return
                webTestClient.get()
                        .uri(uriBuilder -> uriBuilder
                                .path("/authorize/oauth2/v1/authorize")
                                .queryParam("response_type", "code")
                                .queryParam("state", Random.number(16))
                                .build())
                        .exchange()
                        .expectStatus().isOk()
                        .expectBody()
                        .jsonPath("$.data");
    }

    @Test
    void testAuthorize_withResponseType() {
        getCode().value(CodeResponse.class, response -> {
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

    @Test
    void testSendSmsCode() {
        webTestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/authorize/oauth2/v1/authorize")
                        .queryParam("response_type", "sms_code")
                        .queryParam("mobile", "18101845536")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.data")
                .value(data -> {
                    log.debug("send sms result: {}", data);
                });
    }

    @Test
    void testChangePassword() {
        testSendSmsCode();

        smsLoginCodeService.getCode("18101845536")
                        .doOnSuccess(code->{
                            log.debug("sms code: {}", code);

                            webTestClient.get()
                                    .uri(uriBuilder -> uriBuilder
                                            .path("/authorize/oauth2/v1/authorize")
                                            .queryParam("grant_type", GrantType.CHANGE_PASSWORD.getType())
                                            .queryParam("mobile", "18101845536")
                                            .queryParam("code", code)
                                            .queryParam("password", "123456")
                                            .build())
                                    .exchange()
                                    .expectStatus().isOk()
                                    .expectBody()
                                    .jsonPath("$.data")
                                    .value(data -> {
                                        log.debug("send sms result: {}", data);


                                    });
                        }).block();
    }

    @Test
    void testNoExistsChangePassword(){
        testSendSmsCode();

        smsLoginCodeService.getCode("18101845536")
                .doOnSuccess(code->{
                    log.debug("sms code: {}", code);

                    webTestClient.get()
                            .uri(uriBuilder -> uriBuilder
                                    .path("/authorize/oauth2/v1/authorize")
                                    .queryParam("grant_type", GrantType.CHANGE_PASSWORD.getType())
                                    .queryParam("mobile", "18101845536")
                                    .queryParam("code", code)
                                    .queryParam("password", "123456")
                                    .build())
                            .exchange()
                            .expectStatus().is5xxServerError();
                }).block();
    }
}