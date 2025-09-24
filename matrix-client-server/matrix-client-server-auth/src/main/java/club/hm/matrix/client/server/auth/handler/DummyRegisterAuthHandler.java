package club.hm.matrix.client.server.auth.handler;

import club.hm.matrix.client.server.auth.vo.RegisterAvailableResult;
import club.hm.matrix.client.server.common.enums.RegisterAuthType;
import club.hm.matrix.client.server.auth.vo.TokenResponse;
import club.hm.matrix.client.server.auth.vo.RegisterRequest;
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
    public Mono<RegisterAvailableResult> available(String username) {
        return null;
    }

    @Override
    public Mono<TokenResponse> handle(RegisterRequest request) {
        log.info("Dummy auth for username={}", request.getUsername());
        // 模拟生成用户
        var userId = "@%s:matrix.org".formatted(request.getUsername() != null ? request.getUsername() : "randomUser");
        var deviceId = "DEVICE123456";
        var accessToken = "ACCESS_TOKEN_" + System.currentTimeMillis();
        var homeServer = "matrix.org";
        return Mono.just(TokenResponse.builder().userId(userId).accessToken(accessToken).deviceId(deviceId).homeServer(homeServer).build());
    }
}