package club.hm.matrix.auth.grpc.consumer.service.test;

import club.hm.matrix.auth.grpc.CreateUserRequest;
import club.hm.matrix.auth.grpc.consumer.generated.UserAuthorityGrpcClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.test.StepVerifier;

import java.util.UUID;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplication.class)
class UserAuthorityGrpcClientTest {
    @Autowired
    UserAuthorityGrpcClient clientService;

    @Test
    public void testCreateUser()
    {
        log.info("testCreateUser");

        var username = UUID.randomUUID().toString();
        StepVerifier.create(clientService.createUser(CreateUserRequest.newBuilder()
                .setUsername(username)
                .setPassword(username)
                .build()))
                .expectNextMatches(response -> username.equals(response.getUser().getUsername()))
                .expectComplete()
                .verify();
    }
}