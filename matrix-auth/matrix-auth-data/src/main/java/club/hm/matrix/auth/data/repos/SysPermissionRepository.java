package club.hm.matrix.auth.data.repos;

import club.hm.matrix.auth.data.entity.SysPermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysPermissionRepository extends ReactiveCrudRepository<SysPermission, Long> {
}
