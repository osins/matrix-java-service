package io.osins.matrix.user.grpc.consumer.test;

import io.osins.matrix.user.grpc.consumer.service.UserGrpcClientService;
import io.osins.matrix.user.grpc.proto.UserOuterClass;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestConfig.class)
class UserGrpcClientTest {
    @Autowired
    private UserGrpcClientService rpc;

    @Test
    public void testClientCreateUser() {
        log.info("=== 开始调试连接 ===");

        var userId = UUID.randomUUID().toString();
        log.info("生成测试用户ID: {}", userId);

        var userBuilder = UserOuterClass.User.newBuilder()
                .setUserId(userId)
                .setUsername(userId)
                .setPassword("test_password")
                .setEmail(userId + "@homemart.club")
                .setDisplayName("test_display_name")
                .setAvatarUrl("test_avatar_url")
                .setIsActive(true);

        // 不设置任何日期字段，让服务端处理
        var user = userBuilder.build();
        log.info("创建的用户对象: {}", user);

        try {
            StepVerifier.create(
                            rpc.createUser(user)
                                    .doOnSubscribe(subscription -> {
                                        log.info("订阅成功，开始发送请求");
                                    })
                                    .doOnNext(createdUser -> {
                                        log.info("收到响应: {}", createdUser);
                                    })
                                    .doOnError(error -> {
                                        log.error("收到错误响应", error);

                                        if (error instanceof StatusRuntimeException) {
                                            var grpcError = (StatusRuntimeException) error;
                                            log.error("gRPC 状态码: {}", grpcError.getStatus().getCode());
                                            log.error("gRPC 描述: {}", grpcError.getStatus().getDescription());
                                            log.error("gRPC 原因: {}", grpcError.getStatus().getCause());

                                            // 打印所有可用的信息
                                            log.error("完整的 gRPC 状态: {}", grpcError.getStatus());
                                            if (grpcError.getTrailers() != null) {
                                                log.error("gRPC Trailers: {}", grpcError.getTrailers());
                                            }
                                        }
                                    })
                                    .timeout(Duration.ofSeconds(10))
                    )
                    .expectNextCount(1)  // 只验证是否有响应，不验证具体内容
                    .verifyComplete();

            log.info("=== 测试成功完成 ===");

        } catch (Exception e) {
            log.error("=== 测试异常 ===", e);
            throw e;
        }
    }
}