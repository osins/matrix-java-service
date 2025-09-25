package osins.matrix.user.grpc.consumer.generated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import osins.matrix.shared.grpc.base.utils.Observer;
import osins.matrix.user.grpc.proto.UserOuterClass;
@Slf4j
@Service
@RequiredArgsConstructor
public class UserGrpcClient {
    private final reactor.core.publisher.Mono<osins.matrix.user.grpc.proto.UserServiceGrpc.UserServiceStub> userServiceStub;

    public reactor.core.publisher.Mono<osins.matrix.user.grpc.proto.UserOuterClass.User> createUser() {
        return this.userServiceStub.flatMap(stub -> Observer.mono(null, stub::createUser));
    }

    public reactor.core.publisher.Mono<osins.matrix.user.grpc.proto.UserOuterClass.UserListResponse> getAllUsers() {
        return this.userServiceStub.flatMap(stub -> Observer.mono(null, stub::getAllUsers));
    }

    public reactor.core.publisher.Mono<osins.matrix.user.grpc.proto.UserOuterClass.User> getUserById(UserOuterClass.UserIdRequest request) {
        return this.userServiceStub.flatMap(stub -> Observer.mono(request, stub::getUserById));
    }
}
