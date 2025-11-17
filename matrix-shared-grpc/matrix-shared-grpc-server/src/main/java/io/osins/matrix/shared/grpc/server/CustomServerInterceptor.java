package io.osins.matrix.shared.grpc.server;

import io.grpc.ForwardingServerCall;
import io.grpc.ForwardingServerCallListener;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class CustomServerInterceptor implements ServerInterceptor {

    @PostConstruct
    public void init() {
        log.debug("GRPC 服务拦截器 注入成功!");
    }

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
            ServerCall<ReqT, RespT> call,
            Metadata headers,
            ServerCallHandler<ReqT, RespT> next) {

        return new ForwardingServerCallListener.SimpleForwardingServerCallListener<ReqT>(
                next.startCall(new ForwardingServerCall.SimpleForwardingServerCall<ReqT, RespT>(call) {
                    @Override
                    public void close(Status status, Metadata metadata) {
                        log.debug("grpc half interceptor call end, status: {}, metadata: {}", status, metadata);
                        super.close(ReactiveExceptionHandler.converter(status), metadata);
                    }
                }, headers)) {

            @Override
            public void onHalfClose() {
                try {
                    super.onHalfClose();
                } catch (Exception e) {
                    call.close(ReactiveExceptionHandler.converter(e), new Metadata());
                }
            }
        };
    }
}
