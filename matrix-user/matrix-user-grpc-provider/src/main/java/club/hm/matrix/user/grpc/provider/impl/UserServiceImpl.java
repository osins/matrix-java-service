package club.hm.matrix.user.grpc.provider.impl;

import club.hm.matrix.user.data.service.UserDao;
import club.hm.matrix.user.grpc.proto.ReactorUserServiceGrpc;
import club.hm.matrix.user.grpc.proto.UserOuterClass;
import club.hm.matrix.user.grpc.provider.conveter.UserProtoConverter;
import com.google.protobuf.Empty;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserServiceImpl extends ReactorUserServiceGrpc.UserServiceImplBase {

    private final UserDao userDao;
    private final UserProtoConverter userProtoMapper;

    @PostConstruct
    public void init() {
        log.info("=== UserServiceImpl 已初始化 ===");
    }

    @Override
    public Mono<UserOuterClass.User> getUserById(UserOuterClass.UserIdRequest request) {
        return userDao.getUserById(request.getId())
                .map(userProtoMapper::to)
                .doOnError(t -> log.error("获取用户信息失败: {}, {}", request, t.getMessage(), t));
    }

    @Override
    public Mono<UserOuterClass.UserListResponse> getAllUsers(Empty request) {
        return userDao.getAllUsers()
                .map(userProtoMapper::to)
                .collectList()
                .map(list->UserOuterClass.UserListResponse.newBuilder().addAllUsers(list).build());
    }

    @Override
    public Mono<UserOuterClass.User> createUser(UserOuterClass.User request) {
        var userEntity = userProtoMapper.from(request);
        return userDao.createUser(userEntity)
                .map(userProtoMapper::to)
                .doOnError(t -> log.error("创建用户失败: {}, {}", request, t.getMessage(), t));
    }
}