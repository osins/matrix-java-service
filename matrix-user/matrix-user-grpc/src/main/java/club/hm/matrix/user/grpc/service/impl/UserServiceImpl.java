package club.hm.matrix.user.grpc.service.impl;

import club.hm.matrix.user.data.service.UserDao;
import club.hm.matrix.user.grpc.service.UserOuterClass;
import club.hm.matrix.user.grpc.service.UserServiceGrpc;
import club.hm.matrix.user.grpc.service.mapper.UserProtoMapper;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase{
    private final UserDao userDao;
    private final UserProtoMapper userProtoMapper;

    @Override
    public void getUserById(UserOuterClass.UserIdRequest request, StreamObserver<UserOuterClass.User> responseObserver) {
        userDao.getUserById(request.getId())
                .map(userProtoMapper::to)
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @Override
    public void getAllUsers(Empty request, StreamObserver<UserOuterClass.UserListResponse> responseObserver) {
        userDao.getAllUsers()
                .map(userProtoMapper::to)
                .collectList()
                .map(list -> UserOuterClass.UserListResponse.newBuilder().addAllUsers(list).build())
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }

    @Override
    public void createUser(UserOuterClass.User request, StreamObserver<UserOuterClass.User> responseObserver) {
        var userEntity = userProtoMapper.from(request);
        userDao.createUser(userEntity)
                .map(userProtoMapper::to)
                .subscribe(responseObserver::onNext, responseObserver::onError, responseObserver::onCompleted);
    }
}
