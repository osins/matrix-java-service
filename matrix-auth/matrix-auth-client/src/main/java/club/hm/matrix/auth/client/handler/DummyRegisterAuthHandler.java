package club.hm.matrix.auth.client.handler;

import club.hm.matrix.auth.client.enums.RegisterAuthType;
import club.hm.matrix.auth.client.vo.TokenResponse;
import club.hm.matrix.auth.client.vo.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component()
public class DummyRegisterAuthHandler implements RegisterAuthHandler {

    @Override
    public RegisterAuthType getAuthType() {
        return RegisterAuthType.DUMMY;
    }

    @Override
    public Mono<TokenResponse> handle(RegisterRequest request) {
        log.info("Dummy auth for username={}", request.username());
        // 模拟生成用户
        var userId = "@%s:matrix.org".formatted(request.username() != null ? request.username() : "randomUser");
        var deviceId = "DEVICE123456";
        var accessToken = "ACCESS_TOKEN_" + System.currentTimeMillis();
        var homeServer = "matrix.org";
        return Mono.just(TokenResponse.builder().userId(userId).accessToken(accessToken).deviceId(deviceId).homeServer(homeServer).build());
    }
}