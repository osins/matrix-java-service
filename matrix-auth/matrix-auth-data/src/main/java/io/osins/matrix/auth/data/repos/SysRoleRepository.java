package io.osins.matrix.auth.data.repos;

import io.osins.matrix.auth.data.entity.SysRole;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysRoleRepository extends ReactiveCrudRepository<SysRole, Long> {
}
