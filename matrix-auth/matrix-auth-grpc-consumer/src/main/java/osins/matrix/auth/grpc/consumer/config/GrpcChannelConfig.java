package osins.matrix.auth.grpc.consumer.config;

import osins.matrix.shared.grpc.client.ReactiveGrpcServiceDiscovery;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GrpcChannelConfig {
    private final ReactiveGrpcServiceDiscovery grpcServiceDiscovery;

    @Bean
    public Mono<ManagedChannel> reactorAuthGrpcChannel(){
        return grpcServiceDiscovery.getChannel("matrix-auth")
                                .doOnNext(channel -> log.info("User service gRPC channel created successfully"))
                                .doOnError(error -> log.error("Failed to create user service gRPC channel", error))
                                .cache();
    }
}
