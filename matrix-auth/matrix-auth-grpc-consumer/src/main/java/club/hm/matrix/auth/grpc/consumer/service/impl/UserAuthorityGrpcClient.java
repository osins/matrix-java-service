package club.hm.matrix.auth.grpc.consumer.service.impl;

import club.hm.matrix.auth.grpc.*;
import club.hm.matrix.auth.grpc.api.service.UserAuthorityGrpc;
import club.hm.matrix.shared.grpc.base.utils.Observer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserAuthorityGrpcClient implements UserAuthorityGrpc {

    private final Mono<UserAuthorityServiceGrpc.UserAuthorityServiceStub> userAuthorityServiceStubMono;

    // 加载用户
    public Mono<UserResponse> loadUserByUsername(LoadUserByUsernameRequest request) {
        return userAuthorityServiceStubMono.flatMap(stub -> Observer.mono(request, stub::loadUserByUsername));
    }

    // 创建用户
    public Mono<UserResponse> createUser(CreateUserRequest request) {
        return userAuthorityServiceStubMono.flatMap(stub -> Observer.mono(request, stub::createUser));
    }

    // 修改密码
    public Mono<ChangePasswordByUsernameResponse> changePasswordByUsername(ChangePasswordByUsernameRequest request) {
        return userAuthorityServiceStubMono.flatMap(stub -> Observer.mono(request, stub::changePasswordByUsername));
    }
}
