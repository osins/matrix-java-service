package club.hm.matrix.auth.data.repos;

import club.hm.matrix.auth.data.entity.SysRolePath;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SysRolePathRepository extends ReactiveCrudRepository<SysRolePath, Long> {

    // 根据角色查询对应的路径
    Flux<SysRolePath> findByRoleName(String roleName);

    // 根据路径查询对应的角色
    Flux<SysRolePath> findByPathPattern(String pathPattern);
}