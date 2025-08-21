package club.hm.matrix.auth.data.service;

import club.hm.matrix.auth.data.entity.SysPermission;
import club.hm.matrix.auth.data.repos.SysPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SysPermissionService {

    private final SysPermissionRepository permissionRepository;

    public Flux<SysPermission> findAll() {
        return permissionRepository.findAll();
    }

    public Mono<SysPermission> findById(Long permissionId) {
        return permissionRepository.findById(permissionId);
    }

    public Mono<SysPermission> createPermission(SysPermission permission) {
        return permissionRepository.save(permission);
    }

    public Mono<Void> deletePermission(Long permissionId) {
        return permissionRepository.deleteById(permissionId);
    }

    public Mono<Void> deleteById(long id) {
        return permissionRepository.deleteById(id);
    }
}