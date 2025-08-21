package club.hm.matrix.auth.data.service;

import club.hm.matrix.auth.data.entity.SysRolePath;
import club.hm.matrix.auth.data.repos.SysRolePathRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SysRolePathService {

    private final SysRolePathRepository rolePathRepository;

    public Flux<SysRolePath> findAll() {
        return rolePathRepository.findAll();
    }

    public Flux<SysRolePath> findByRole(String roleName) {
        return rolePathRepository.findByRoleName(roleName);
    }

    public Flux<SysRolePath> findByPath(String pathPattern) {
        return rolePathRepository.findByPathPattern(pathPattern);
    }

    public Mono<SysRolePath> createRolePath(Long id, String roleName, String pathPattern, String httpMethod) {
        var rolePath = new SysRolePath(id, roleName, pathPattern, httpMethod, LocalDateTime.now(), LocalDateTime.now());
        return rolePathRepository.save(rolePath);
    }

    public Mono<Void> deleteById(Long id) {
        return rolePathRepository.deleteById(id);
    }

    public Mono<SysRolePath> updateRolePath(SysRolePath updated) {
        // 更新时间
        SysRolePath rolePath = new SysRolePath(
                updated.id(),
                updated.roleName(),
                updated.pathPattern(),
                updated.httpMethod(),
                updated.createdAt(),
                LocalDateTime.now()
        );
        return rolePathRepository.save(rolePath);
    }
}