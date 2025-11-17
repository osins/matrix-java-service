package io.osins.matrix.auth.data.repos;

import io.osins.matrix.auth.data.entity.SysRolePermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SysRolePermissionRepository extends ReactiveCrudRepository<SysRolePermission, Long> {
    Flux<SysRolePermission> findByRoleId(Long roleId);
}
