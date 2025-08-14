package club.hm.matrix.user.grpc.consumer.service;

import club.hm.matrix.user.grpc.proto.UserOuterClass;
import club.hm.matrix.user.grpc.proto.UserServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserGrpcClientService {
    private final Mono<UserServiceGrpc.UserServiceStub> asyncStub;

    public Mono<UserOuterClass.User> createUser(UserOuterClass.User user) {
        return asyncStub.flatMap(stub -> Mono.<UserOuterClass.User>create(sink -> {
                    stub.createUser(user, new StreamObserver<>() {
                        @Override
                        public void onNext(UserOuterClass.User value) {
                            sink.success(value); // 推送数据
                        }

                        @Override
                        public void onError(Throwable t) {
                            sink.error(t); // 推送错误
                        }

                        @Override
                        public void onCompleted() {
                        }
                    });
                }))
                .doOnError(error -> log.error("Async call failed for user: {}", user, error))
                .doOnNext(response -> log.info("Async call completed for user: {}", user));

    }


    /**
     * 根据 ID 获取用户
     */
    public Mono<UserOuterClass.User> getUserById(long id) {
        return asyncStub.flatMap(stub -> Mono.create(sink -> {
            stub.getUserById(
                    UserOuterClass.UserIdRequest.newBuilder().setId(id).build(),
                    new StreamObserver<>() {
                        @Override
                        public void onNext(UserOuterClass.User value) {
                            sink.success(value);
                        }

                        @Override
                        public void onError(Throwable t) {
                            sink.error(t);
                        }

                        @Override
                        public void onCompleted() {
                        }
                    }
            );
        }));
    }

    /**
     * 获取所有用户（转换为 Flux）
     */
    public Flux<UserOuterClass.User> getAllUsers() {
        return asyncStub.flatMapMany(stub ->
                Flux.create(sink -> {
                    stub.getAllUsers(Empty.getDefaultInstance(), new StreamObserver<>() {
                        @Override
                        public void onNext(UserOuterClass.UserListResponse value) {
                            value.getUsersList().forEach(sink::next);
                        }

                        @Override
                        public void onError(Throwable t) {
                            sink.error(t);
                        }

                        @Override
                        public void onCompleted() {
                            sink.complete();
                        }
                    });
                })
        );
    }
}
