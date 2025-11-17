package io.osins.matrix.auth.data.service;

import io.osins.matrix.auth.data.entity.SysRole;
import io.osins.matrix.auth.data.repos.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class SysRoleService {

    private final SysRoleRepository roleRepository;

    public Flux<SysRole> findAll() {
        return roleRepository.findAll();
    }

    public Mono<SysRole> findById(Long roleId) {
        return roleRepository.findById(roleId);
    }

    public Mono<SysRole> createRole(SysRole role) {
        return roleRepository.save(role);
    }

    public Mono<Void> deleteRole(Long roleId) {
        return roleRepository.deleteById(roleId);
    }

    public Mono<Void> deleteById(long id) {
        return roleRepository.deleteById(id);
    }
}