package club.hm.matrix.user.grpc.consumer.service;

import club.hm.matrix.user.grpc.proto.ReactorUserServiceGrpc;
import club.hm.matrix.user.grpc.proto.UserOuterClass;
import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserGrpcClientService {

    private final Mono<ReactorUserServiceGrpc.ReactorUserServiceStub> reactorStub;

    /**
     * 创建用户
     */
    public Mono<UserOuterClass.User> createUser(UserOuterClass.User user) {
        return reactorStub
                .flatMap(stub -> stub.createUser(user))
                .doOnError(error -> log.error("Create user failed: {}", user, error))
                .doOnNext(resp -> log.info("Create user success: {}", resp));
    }

    /**
     * 根据 ID 获取用户
     */
    public Mono<UserOuterClass.User> getUserById(long id) {
        return reactorStub
                .flatMap(stub -> stub.getUserById(
                        UserOuterClass.UserIdRequest.newBuilder().setId(id).build()
                ))
                .doOnError(error -> log.error("Get user by ID failed: {}", id, error));
    }

    /**
     * 获取所有用户（转换为 Flux）
     */
    public Flux<UserOuterClass.User> getAllUsers() {
        return reactorStub
                .flatMapMany(stub -> stub.getAllUsers(Empty.getDefaultInstance())
                        .flatMapIterable(UserOuterClass.UserListResponse::getUsersList)
                )
                .doOnError(error -> log.error("Get all users failed", error));
    }
}
