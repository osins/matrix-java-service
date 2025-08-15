package club.hm.matrix.auth.security.service;

import club.hm.matrix.auth.security.domain.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReactiveUserDetailsService {

    public Mono<CustomPrincipal> findByUsername(String username) {
        return Mono.just(CustomPrincipal.builder().userId(1L).username("wahaha").build());
    }

    private Mono<CustomPrincipal> createUserDetails(CustomPrincipal user) {
        return Mono.just(CustomPrincipal.builder().userId(1L).username("wahaha").build());
    }
}