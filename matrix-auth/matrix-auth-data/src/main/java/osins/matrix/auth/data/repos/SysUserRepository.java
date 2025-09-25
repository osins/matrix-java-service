package osins.matrix.auth.data.repos;

import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import osins.matrix.auth.data.entity.SysUser;
import reactor.core.publisher.Mono;

public interface SysUserRepository extends ReactiveCrudRepository<SysUser, Long> {
    Mono<SysUser> findByUsername(String username);

    @Modifying
    @Query("UPDATE sys_user SET password = :password WHERE username = :username")
    Mono<Integer> changePasswordByUsername(@Param("username") String username,@Param("password") String newPassword);

    Mono<Boolean> existsByUsername(String username);
}

