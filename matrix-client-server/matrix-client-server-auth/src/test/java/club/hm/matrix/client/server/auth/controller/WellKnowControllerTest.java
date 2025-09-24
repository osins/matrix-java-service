package club.hm.matrix.client.server.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@Slf4j
@WebFluxTest({WellKnowController.class, ClientController.class})
public class WellKnowControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testGetWellKnow() {
        var responseBody = webTestClient.get()
                .uri("/.well-known/matrix/client")
                .exchange()
                .expectStatus().isOk()
                .expectBody();

        log.info("responseBody: {}", new String(Objects.requireNonNull(responseBody.returnResult().getResponseBody())));

        responseBody
                // 测试 m.homeserver.base_url
                .jsonPath("$['m.homeserver'].base_url").isEqualTo("https://matrix.example.com")
                // 测试 m.identity_server.base_url
                .jsonPath("$['m.identity_server'].base_url").isEqualTo("https://identity.example.com")
                // 测试 org.example.custom.property.app_url
                .jsonPath("$['org.example.custom.property'].app_url").isEqualTo("https://custom.app.example.org");
    }

    @Test
    void testGetWellKnowSupport() {
        webTestClient.get()
                .uri("/.well-known/matrix/support")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    // 打印返回 body 内容
                    System.out.println(new String(response.getResponseBody()));
                })
                // 验证 JSON 内容
                .jsonPath("$.contacts[0].email_address").isEqualTo("admin@example.org")
                .jsonPath("$.contacts[0].matrix_id").isEqualTo("@admin:example.org")
                .jsonPath("$.contacts[0].role").isEqualTo("m.role.admin")
                .jsonPath("$.contacts[1].email_address").isEqualTo("security@example.org")
                .jsonPath("$.contacts[1].matrix_id").doesNotExist()
                .jsonPath("$.contacts[1].role").isEqualTo("m.role.security")
                .jsonPath("$.support_page").isEqualTo("https://example.org/support.html");
    }

    @Test
    void testGetVersions() {
        webTestClient.get()
                .uri("/_matrix/client/versions")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .consumeWith(response -> {
                    System.out.println(new String(response.getResponseBody()));
                })
                .jsonPath("$.unstable_features['org.example.my_feature']").isEqualTo(true)
                .jsonPath("$.versions[0]").isEqualTo("r0.0.1")
                .jsonPath("$.versions[1]").isEqualTo("v1.1");
    }
}