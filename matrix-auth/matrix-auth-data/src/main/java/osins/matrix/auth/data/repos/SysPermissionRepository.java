package osins.matrix.auth.data.repos;

import osins.matrix.auth.data.entity.SysPermission;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysPermissionRepository extends ReactiveCrudRepository<SysPermission, Long> {
}
