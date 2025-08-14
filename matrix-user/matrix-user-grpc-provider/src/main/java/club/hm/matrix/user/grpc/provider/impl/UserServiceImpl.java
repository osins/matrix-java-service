package club.hm.matrix.user.grpc.provider.impl;

import club.hm.matrix.user.data.service.UserDao;
import club.hm.matrix.user.grpc.proto.UserOuterClass;
import club.hm.matrix.user.grpc.proto.UserServiceGrpc;
import club.hm.matrix.user.grpc.provider.mapper.UserProtoMapper;
import com.google.protobuf.Empty;
import io.grpc.Context;
import io.grpc.override.ReactorContextHolder;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase{

    private final UserDao userDao;
    private final UserProtoMapper userProtoMapper;

    @PostConstruct
    public void init() {
        log.info("=== UserServiceImpl 已初始化 ===");
    }

    @Override
    public void getUserById(UserOuterClass.UserIdRequest request, StreamObserver<UserOuterClass.User> responseObserver) {
        userDao.getUserById(request.getId())
                .map(userProtoMapper::to)
                .doOnError(t -> log.error("获取用户信息失败: {}, {}", request, t.getMessage(), t))
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @Override
    public void getAllUsers(Empty request, StreamObserver<UserOuterClass.UserListResponse> responseObserver) {
        userDao.getAllUsers()
                .map(userProtoMapper::to)
                .collectList()
                .map(list -> UserOuterClass.UserListResponse.newBuilder().addAllUsers(list).build())
                .doOnError(t -> log.error("获取用户列表失败: {}, {}", request, t.getMessage(), t))
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @Override
    public void createUser(UserOuterClass.User request, StreamObserver<UserOuterClass.User> responseObserver) {
        // 获取当前 gRPC Context
        var grpcCtx = Context.current();

        var userEntity = userProtoMapper.from(request);
        userDao.createUser(userEntity)
                .map(userProtoMapper::to)
                .contextWrite(ctx -> ctx.put(Context.class, grpcCtx))
                .doOnEach(signal -> ReactorContextHolder.setReactorContext(signal.getContextView()))
                .doFinally(sig -> ReactorContextHolder.clear())
                .doOnError(t -> log.error("创建用户失败: {}, {}", request, t.getMessage(), t))
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }
}
