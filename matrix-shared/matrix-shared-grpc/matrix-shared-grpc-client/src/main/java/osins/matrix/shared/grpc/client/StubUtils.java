package osins.matrix.shared.grpc.client;

import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

@Slf4j
public final class StubUtils {
    private static final long DEFAULT_DEADLINE_SEC = 5;

    public static <T extends io.grpc.stub.AbstractStub<T>> Mono<T> newStub(Mono<ManagedChannel> channel, Function<ManagedChannel, T> call) {
        return channel
                .map(c -> call.apply(c)
                        .withWaitForReady()
                        .withDeadline(Deadline.after(DEFAULT_DEADLINE_SEC, TimeUnit.SECONDS)))
                .cache();
    }

    public static <T extends io.grpc.stub.AbstractBlockingStub<T>> Mono<T> newBlockingStub(Mono<ManagedChannel> channel, Function<ManagedChannel, T> call) {
        return channel
                .map(c -> call.apply(c)
                        .withWaitForReady()
                        .withDeadline(Deadline.after(DEFAULT_DEADLINE_SEC, TimeUnit.SECONDS)))
                .cache();
    }

    public static <T extends io.grpc.stub.AbstractFutureStub<T>> Mono<T> newFutureStub(Mono<ManagedChannel> channel, Function<ManagedChannel, T> call) {
        return channel
                .map(c -> call.apply(c)
                        .withWaitForReady()
                        .withDeadline(Deadline.after(DEFAULT_DEADLINE_SEC, TimeUnit.SECONDS)))
                .cache();
    }

    public static <T extends io.grpc.stub.AbstractAsyncStub<T>> Mono<T> newAsyncStub(Mono<ManagedChannel> channel, Function<ManagedChannel, T> call) {
        return channel
                .map(c -> call.apply(c)
                        .withWaitForReady()
                        .withDeadline(Deadline.after(DEFAULT_DEADLINE_SEC, TimeUnit.SECONDS)))
                .cache();
    }
}
