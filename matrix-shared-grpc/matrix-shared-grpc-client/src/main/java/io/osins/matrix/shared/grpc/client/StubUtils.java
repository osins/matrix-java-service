package io.osins.matrix.shared.grpc.client;

import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Slf4j
public final class StubUtils {
    public static <T extends io.grpc.stub.AbstractStub<T>> Mono<T> newStub(Mono<ManagedChannel> channel, Function<ManagedChannel, T> call) {
        return channel
                .doOnNext(c -> log.info("User service gRPC channel created successfully"))
                .doOnError(error -> log.error("Failed to create user service gRPC channel", error))
                .cache()
                .map(c -> call.apply(c).withWaitForReady())
                .cache();
    }
    public static <T extends io.grpc.stub.AbstractBlockingStub<T>> Mono<T> newBlockingStub(Mono<ManagedChannel> channel, Function<ManagedChannel, T> call) {
        return channel
                .doOnNext(c -> log.info("User service gRPC channel created successfully"))
                .doOnError(error -> log.error("Failed to create user service gRPC channel", error))
                .cache()
                .map(c -> call.apply(c).withWaitForReady())
                .cache();
    }
    public static <T extends io.grpc.stub.AbstractFutureStub<T>> Mono<T> newFutureStub(Mono<ManagedChannel> channel, Function<ManagedChannel, T> call) {
        return channel
                .doOnNext(c -> log.info("User service gRPC channel created successfully"))
                .doOnError(error -> log.error("Failed to create user service gRPC channel", error))
                .cache()
                .map(c -> call.apply(c).withWaitForReady())
                .cache();
    }
    public static <T extends io.grpc.stub.AbstractAsyncStub<T>> Mono<T> newAsyncStub(Mono<ManagedChannel> channel, Function<ManagedChannel, T> call) {
        return channel
                .doOnNext(c -> log.info("User service gRPC channel created successfully"))
                .doOnError(error -> log.error("Failed to create user service gRPC channel", error))
                .cache()
                .map(c -> call.apply(c).withWaitForReady())
                .cache();
    }
}
