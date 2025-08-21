package club.hm.matrix.auth.data.service;

import club.hm.matrix.auth.data.entity.SysRolePermission;
import club.hm.matrix.auth.data.repos.SysRolePermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SysRolePermissionService {

    private final SysRolePermissionRepository rolePermissionRepository;

    public Flux<SysRolePermission> findPermissionsByRoleId(Long roleId) {
        return rolePermissionRepository.findByRoleId(roleId);
    }

    public Mono<SysRolePermission> assignPermissionToRole(Long roleId, Long permissionId) {
        return rolePermissionRepository.save(new SysRolePermission(roleId, permissionId, LocalDateTime.now(), LocalDateTime.now()));
    }

    public Mono<Void> removePermissionFromRole(Long roleId, Long permissionId) {
        return rolePermissionRepository.findByRoleId(roleId)
                .filter(rp -> rp.permissionId().equals(permissionId))
                .flatMap(rolePermissionRepository::delete)
                .then();
    }
}