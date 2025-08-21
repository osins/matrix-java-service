package club.hm.matrix.shared.grpc.base.utils;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.function.BiConsumer;

@Slf4j
public final class Observer {

    private Observer() {
    }

    public static <R extends com.google.protobuf.GeneratedMessageV3> Mono<R> mono(BiConsumer<Empty, StreamObserver<R>> obs) {
        log.debug("grpc start");
        return Mono.create(sink -> {
            try {
                obs.accept(Empty.getDefaultInstance(), new StreamObserver<R>() {
                    @Override
                    public void onNext(R o) {
                        log.debug("grpc next: {}", Gjson.toJSONString(o));
                        sink.success(o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error("grpc error: {}", t.getMessage());
                        sink.error(t);
                    }

                    @Override
                    public void onCompleted() {
                        log.debug("grpc completed");
                    }
                });
            } catch (Exception ex) {
                log.error("grpc error: {}", ex.getMessage());
                sink.error(ex);
            }
        });
    }

    public static <T extends com.google.protobuf.GeneratedMessageV3, R extends com.google.protobuf.GeneratedMessageV3> Mono<R> mono(T request, BiConsumer<T, StreamObserver<R>> obs) {
        log.debug("grpc start, request: {}", Gjson.toJSONString(request));

        return Mono.create(sink -> {
            try {
                obs.accept(request, new StreamObserver<R>() {
                    @Override
                    public void onNext(R o) {
                        log.debug("grpc next: {}, request: {}", o, Gjson.toJSONString(request));
                        sink.success(o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error("grpc error: {}, request: {}", t.getMessage(), Gjson.toJSONString(request), t);
                        sink.error(t);
                    }

                    @Override
                    public void onCompleted() {
                        log.debug("grpc completed: {}", Gjson.toJSONString(request));
                    }
                });
            } catch (Exception ex) {
                log.error("grpc error: {}, request: {}", ex.getMessage(), Gjson.toJSONString(request), ex);
                sink.error(ex);
            }
        });
    }

    public static <R extends com.google.protobuf.GeneratedMessageV3> void provider(Mono<R> mono, StreamObserver<R> obs) {
        mono.doOnNext(data -> {
                    log.debug("provider next: {}", data);
                })
                .doOnError(throwable -> {
                    log.error("{}", throwable.getMessage());
                    obs.onError(
                            Status.INTERNAL
                                    .withDescription(throwable.getMessage())
                                    .asRuntimeException()
                    );
                })
                .subscribe(response -> {
                    log.debug("changePasswordByUsername success: {}", Gjson.toJSONString(response));
                    obs.onNext(response);
                    obs.onCompleted();
                });
    }

    public static <R extends com.google.protobuf.GeneratedMessageV3> Flux<R> flux(BiConsumer<Empty, StreamObserver<R>> obs) {
        log.debug("grpc flux start");
        return Flux.create(sink -> {
            try {
                obs.accept(Empty.getDefaultInstance(), new StreamObserver<R>() {
                    @Override
                    public void onNext(R o) {
                        log.debug("grpc flux next: {}", Gjson.toJSONString(o));
                        sink.next(o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error("grpc flux error: {}", t.getMessage(), t);
                        sink.error(t);
                    }

                    @Override
                    public void onCompleted() {
                        log.error("grpc flux completed");
                        sink.complete();
                    }
                });
            } catch (Exception ex) {
                log.error("grpc flux error: {}", ex.getMessage(), ex);
                sink.error(ex);
            }
        }, FluxSink.OverflowStrategy.BUFFER);
    }

    public static <T extends com.google.protobuf.GeneratedMessageV3, R extends com.google.protobuf.GeneratedMessageV3> Flux<R> flux(T request, BiConsumer<T, StreamObserver<R>> obs) {
        log.debug("grpc flux request: {}", Gjson.toJSONString(request));
        return Flux.create(sink -> {
            try {
                obs.accept(request, new StreamObserver<R>() {
                    @Override
                    public void onNext(R o) {
                        log.debug("grpc flux response: {}, request: {}", Gjson.toJSONString(o), Gjson.toJSONString(request));
                        sink.next(o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        log.error("grpc flux error: {}, request: {}", t.getMessage(), Gjson.toJSONString(request), t);
                        sink.error(t);
                    }

                    @Override
                    public void onCompleted() {
                        log.error("grpc flux completed: {}", Gjson.toJSONString(request));
                        sink.complete();
                    }
                });
            } catch (Exception ex) {
                sink.error(ex);
            }
        }, FluxSink.OverflowStrategy.BUFFER);
    }

    public static <R extends com.google.protobuf.GeneratedMessageV3> void provider(Flux<R> flux, StreamObserver<R> obs) {
        flux.doOnNext(data -> {
                    log.debug("grpc server next: {}", data);
                })
                .doOnError(throwable -> {
                    log.error("{}", throwable.getMessage());
                    obs.onError(Status.INTERNAL
                                    .withDescription(throwable.getMessage())
                                    .asRuntimeException()
                    );
                })
                .subscribe(response -> {
                    log.debug("grpc server flux success: {}", Gjson.toJSONString(response));
                    obs.onNext(response);
                    obs.onCompleted();
                });
    }
}
