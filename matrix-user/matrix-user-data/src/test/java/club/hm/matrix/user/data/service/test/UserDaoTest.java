package club.hm.matrix.user.data.service.test;

import club.hm.matrix.user.data.entity.User;
import club.hm.matrix.user.data.service.UserDao;
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
@SpringBootTest(classes = TestConfig.class)
class UserDaoTest {
    @Autowired
    private UserDao userDao;

    @Test
    public void testGetUserById() {
        var userId = UUID.randomUUID().toString();
        StepVerifier.create(userDao.createUser(User.builder()
                                .userId(userId)
                                .username(userId)
                                .password("test_password")
                                .email(userId+"@homemart.club")
                                .displayName("test_display_name")
                                .avatarUrl("test_avatar_url")
                                .isActive(true)
                                .build())
                        .flatMap(user -> userDao.getUserById(user.getId())))
                .expectNextMatches(user -> {
                    log.info("user: {}", user);
                    return user != null;
                })
                .verifyComplete();
    }
}