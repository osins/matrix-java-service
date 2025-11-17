package io.osins.matrix.user.grpc.consumer.service;

import io.osins.matrix.shared.grpc.base.utils.Observer;
import io.osins.matrix.user.grpc.proto.UserOuterClass;
import io.osins.matrix.user.grpc.proto.UserServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserGrpcClientService {

    private final Mono<UserServiceGrpc.UserServiceStub> reactorStub;

    /**
     * 创建用户
     */
    public Mono<UserOuterClass.User> createUser(UserOuterClass.User user) {
        return reactorStub
                .flatMap(stub -> Observer.mono(user, stub::createUser))
                .doOnError(error -> log.error("Create user failed: {}", user, error))
                .doOnNext(resp -> log.info("Create user success: {}", resp));
    }

    /**
     * 根据 ID 获取用户
     */
    public Mono<UserOuterClass.User> getUserById(long id) {
        var request = UserOuterClass.UserIdRequest.newBuilder().setId(id).build();
        return reactorStub
                .flatMap(stub -> Observer.mono(request, stub::getUserById))
                .doOnError(error -> log.error("Get user by ID failed: {}", id, error));
    }

    /**
     * 获取所有用户（转换为 Flux）
     */
    public Flux<UserOuterClass.UserListResponse> getAllUsers() {
        return reactorStub
                .flatMapMany(stub -> Observer.flux(stub::getAllUsers));
    }
}
