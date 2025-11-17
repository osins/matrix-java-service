package io.osins.matrix.shared.grpc.server;

import io.osins.matrix.shared.data.exception.DuplicateException;
import io.osins.matrix.shared.data.exception.NotFoundException;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Component;
import jakarta.validation.ValidationException;

import javax.naming.ServiceUnavailableException;

@Slf4j
@Component
public class ReactiveExceptionHandler {

    public static Status converter(Status status) {
        if (status.getCode() == Status.Code.OK || status.getCause() == null) {
            return status;
        }

        return converter(status.getCause());
    }

    public static Status converter(Throwable throwable) {
        log.error("GRPC异常: {}", throwable.getMessage());

        return switch (throwable) {
            case StatusRuntimeException statusRuntimeException ->
                    with(Status.INTERNAL, throwable);
            case NotFoundException ignored ->
                    with(Status.NOT_FOUND, throwable);
            case DuplicateException ignored ->
                    with(Status.ALREADY_EXISTS, throwable);
            case DataRetrievalFailureException ignored ->
                    with(Status.INTERNAL, throwable);
            case IllegalArgumentException ignored ->
                    with(Status.INVALID_ARGUMENT, throwable);
            case ServiceUnavailableException ignored ->
                    with(Status.UNAVAILABLE, throwable);
            case ValidationException ignored ->
                    with(Status.INVALID_ARGUMENT, throwable);
            default ->
                // 未知异常统一处理为INTERNAL错误
                    Status.INTERNAL.withDescription("GRPC 服务内部错误: " + throwable.getMessage()).withCause(throwable);
        };
    }

    private static Status with(Status status, Throwable throwable){
        return status.withDescription(throwable.getMessage()).withCause(throwable);
    }
}
