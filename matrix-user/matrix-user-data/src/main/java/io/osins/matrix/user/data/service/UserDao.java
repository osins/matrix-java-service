package io.osins.matrix.user.data.service;

import io.osins.matrix.shared.snowflake.id.generator.BufferedIdService;
import io.osins.matrix.user.data.entity.User;
import io.osins.matrix.user.data.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserDao {
    private final R2dbcEntityTemplate template;
    private final UserRepository userRepository;
    private final BufferedIdService bufferedIdService;

    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> createUser(User user) {
        if(user.getId() == null || user.getId() <= 0) {
            user.setId(bufferedIdService.getNextId());
            return template.insert(User.class).using(user);
        }

        return userRepository.save(user);
    }
}
