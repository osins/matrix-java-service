package club.hm.matrix.auth.data.service;


import club.hm.matrix.auth.data.entity.SysUserRole;
import club.hm.matrix.auth.data.repos.SysUserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SysUserRoleService {

    private final SysUserRoleRepository userRoleRepository;

    public Flux<SysUserRole> findRolesByUserId(Long userId) {
        return userRoleRepository.findByUserId(userId);
    }

    public Mono<SysUserRole> assignRoleToUser(Long userId, Long roleId) {
        return userRoleRepository.save(new SysUserRole(userId, roleId, LocalDateTime.now(), LocalDateTime.now()));
    }

    public Mono<Void> removeRoleFromUser(Long userId, Long roleId) {
        return userRoleRepository.findByUserId(userId)
                .filter(ur -> ur.roleId().equals(roleId))
                .flatMap(userRoleRepository::delete)
                .then();
    }
}