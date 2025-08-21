package club.hm.matrix.auth.grpc.api.service;

import club.hm.matrix.auth.grpc.*;
import reactor.core.publisher.Mono;

/**
 * gRPC Reactive 客户端接口
 */
public interface UserAuthorityGrpc  {
    Mono<UserResponse> loadUserByUsername(LoadUserByUsernameRequest request);
    Mono<UserResponse> createUser(CreateUserRequest request);
    Mono<ChangePasswordByUsernameResponse> changePasswordByUsername(ChangePasswordByUsernameRequest request);
}