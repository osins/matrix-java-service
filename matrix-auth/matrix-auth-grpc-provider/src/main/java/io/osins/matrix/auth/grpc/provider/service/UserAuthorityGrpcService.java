package io.osins.matrix.auth.grpc.provider.service;

import io.osins.matrix.auth.data.service.UserAuthorityService;
import io.osins.matrix.auth.grpc.*;
import io.osins.matrix.auth.grpc.*;
import io.osins.matrix.auth.grpc.provider.converter.UserAuthorityGrpcConverter;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Mono;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserAuthorityGrpcService extends UserAuthorityServiceGrpc.UserAuthorityServiceImplBase{

    private final UserAuthorityService userAuthorityService;
    private final UserAuthorityGrpcConverter converter;

    @PostConstruct
    public void init() {
        log.info("UserAuthorityGrpcService init");
    }

    @Override
    public void loadUserByUsername(LoadUserByUsernameRequest request,
                                   StreamObserver<UserResponse> responseObserver) {
        userAuthorityService.loadUserByUsername(request.getUsername())
                .map(converter::toGrpcUser)
                .switchIfEmpty(Mono.just(UserResponse.newBuilder().setUser(User.newBuilder().build()).build()))
                .doOnError(throwable -> {
                    log.error("loadUserByUsername error", throwable);
                    responseObserver.onError(
                            Status.INTERNAL.withDescription(throwable.getMessage()).asRuntimeException()
                    );
                })
                .doOnNext(userResponse -> {
                    log.info("loadUserByUsername success: {}", userResponse);
                    responseObserver.onNext(userResponse);
                    responseObserver.onCompleted();
                })
                .subscribe();
    }

    @Override
    public void createUser(CreateUserRequest request,
                           StreamObserver<UserResponse> responseObserver) {
        userAuthorityService.createUser(converter.fromGrpcUser(request))
                .map(converter::toGrpcUser)
                .subscribe(
                        userResponse -> {
                            log.info("createUser success: {}", userResponse);
                            responseObserver.onNext(userResponse);
                            responseObserver.onCompleted();
                        },
                        ex -> {
                            log.error("创建用户失败: {}, {}", request, ex.getMessage(), ex);
                            if (ex instanceof IllegalArgumentException) {
                                responseObserver.onError(Status.INVALID_ARGUMENT
                                        .withDescription(ex.getMessage()).asRuntimeException());
                            } else if (ex instanceof DuplicateKeyException) {
                                responseObserver.onError(Status.ALREADY_EXISTS
                                        .withDescription(ex.getMessage()).asRuntimeException());
                            } else {
                                responseObserver.onError(Status.INTERNAL
                                        .withDescription(ex.getMessage()).asRuntimeException());
                            }
                        }
                );
    }

    @Override
    public void changePasswordByUsername(ChangePasswordByUsernameRequest request,
                                         StreamObserver<ChangePasswordByUsernameResponse> responseObserver) {
        userAuthorityService.changePasswordByUsername(request.getUsername(), request.getNewPassword())
                .map(changes -> ChangePasswordByUsernameResponse.newBuilder()
                        .setSuccess(true)
                        .setChanges(changes)
                        .build())
                .subscribe(
                        response -> {
                            log.info("changePasswordByUsername success: {}", response);
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        },
                        throwable -> {
                            log.error("changePasswordByUsername error", throwable);
                            responseObserver.onError(Status.INTERNAL
                                    .withDescription(throwable.getMessage()).asRuntimeException());
                        }
                );
    }
}