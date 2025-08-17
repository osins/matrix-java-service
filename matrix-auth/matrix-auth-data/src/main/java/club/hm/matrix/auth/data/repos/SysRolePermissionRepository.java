package club.hm.matrix.auth.data.repos;

import club.hm.matrix.auth.data.entity.SysRolePermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SysRolePermissionRepository extends ReactiveCrudRepository<SysRolePermission, Long> {
    Flux<SysRolePermission> findByRoleId(Long roleId);
}
