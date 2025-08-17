package club.hm.matrix.auth.data.repos;

import club.hm.matrix.auth.data.entity.SysUserRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SysUserRoleRepository extends ReactiveCrudRepository<SysUserRole, Long> {
    Flux<SysUserRole> findByUserId(Long userId);
}
