package club.hm.matrix.shared.grpc.base.utils;

import club.hm.homemart.club.shared.common.uitls.Result;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import reactor.core.publisher.Mono;

public final class StatusConverter {
    public static Status to(Throwable throwable){
        if(throwable instanceof StatusRuntimeException)
            return ((StatusRuntimeException) throwable).getStatus();

        return Status.INTERNAL.withDescription(throwable.getMessage()).withCause(throwable);
    }

    public static String description(Throwable throwable){
        if(throwable instanceof StatusRuntimeException)
            return ((StatusRuntimeException) throwable).getStatus().getDescription();

        return throwable.getMessage();
    }

    public static Mono<String> descriptionMono(Throwable throwable){
        return Mono.just(description(throwable));
    }

    public static <R> Mono<Result<R>> error(Throwable throwable){
        return descriptionMono(throwable).map(Result::failure);
    }
}
