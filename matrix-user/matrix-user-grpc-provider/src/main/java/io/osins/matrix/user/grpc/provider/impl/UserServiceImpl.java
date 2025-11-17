package io.osins.matrix.user.grpc.provider.impl;

import io.osins.matrix.shared.grpc.base.utils.Observer;
import io.osins.matrix.user.data.service.UserDao;
import io.osins.matrix.user.grpc.proto.UserOuterClass;
import io.osins.matrix.user.grpc.proto.UserServiceGrpc;
import io.osins.matrix.user.grpc.provider.conveter.UserProtoConverter;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    private final UserDao userDao;
    private final UserProtoConverter userProtoMapper;

    @PostConstruct
    public void init() {
        log.info("=== UserServiceImpl 已初始化 ===");
    }

    @Override
    public void getUserById(UserOuterClass.UserIdRequest request, StreamObserver<UserOuterClass.User> obs) {
        Observer.provider(userDao.getUserById(request.getId())
                        .map(userProtoMapper::to), obs);
    }

    @Override
    public void getAllUsers(Empty request, StreamObserver<UserOuterClass.UserListResponse> obs) {
        Observer.provider(userDao.getAllUsers()
                .map(userProtoMapper::to)
                .collectList()
                .map(list->UserOuterClass.UserListResponse.newBuilder().addAllUsers(list).build()), obs);
    }

    @Override
    public void createUser(UserOuterClass.User request, StreamObserver<UserOuterClass.User> obs) {
        var userEntity = userProtoMapper.from(request);
        Observer.provider(userDao.createUser(userEntity)
                .map(userProtoMapper::to)
                .doOnError(t -> log.error("创建用户失败: {}, {}", request, t.getMessage(), t)), obs);
    }
}