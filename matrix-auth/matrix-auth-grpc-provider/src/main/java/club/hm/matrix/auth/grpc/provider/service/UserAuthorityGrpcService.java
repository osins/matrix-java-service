package club.hm.matrix.auth.grpc.provider.service;

import club.hm.matrix.auth.data.service.UserAuthorityService;
import club.hm.matrix.auth.grpc.*;
import club.hm.matrix.auth.grpc.api.service.UserAuthorityGrpc;
import club.hm.matrix.auth.grpc.provider.converter.UserAuthorityGrpcConverter;
import io.grpc.Status;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.dao.DuplicateKeyException;
import reactor.core.publisher.Mono;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserAuthorityGrpcService extends ReactorUserAuthorityServiceGrpc.UserAuthorityServiceImplBase implements UserAuthorityGrpc {

    private final UserAuthorityService userAuthorityService;
    private final UserAuthorityGrpcConverter converter;

    @PostConstruct
    public void init() {
        log.info("UserAuthorityGrpcService init");
    }

    @Override
    public Mono<UserResponse> loadUserByUsername(LoadUserByUsernameRequest request) {
        return userAuthorityService.loadUserByUsername(request.getUsername())
                .map(converter::toGrpcUser)
                .doOnError(throwable -> log.error("loadUserByUsername error", throwable))
                .doOnNext(userResponse -> log.info("loadUserByUsername success: {}", userResponse));
    }

    @Override
    public Mono<UserResponse> createUser(CreateUserRequest request) {
        return userAuthorityService.createUser(converter.fromGrpcUser(request.getUser()))
                .map(converter::toGrpcUser)
                .doOnError(t -> log.error("创建用户失败: {}, {}", request, t.getMessage(), t))
                .onErrorMap(ex -> {
                    if (ex instanceof IllegalArgumentException) {
                        // 参数错误 → INVALID_ARGUMENT
                        return Status.INVALID_ARGUMENT.withDescription(ex.getMessage()).asRuntimeException();
                    } else if (ex instanceof DuplicateKeyException) {
                        // 数据库重复 → ALREADY_EXISTS
                        return Status.ALREADY_EXISTS.withDescription(ex.getMessage()).asRuntimeException();
                    } else {
                        // 其他 → INTERNAL
                        return Status.INTERNAL.withDescription(ex.getMessage()).asRuntimeException();
                    }
                });
    }
}