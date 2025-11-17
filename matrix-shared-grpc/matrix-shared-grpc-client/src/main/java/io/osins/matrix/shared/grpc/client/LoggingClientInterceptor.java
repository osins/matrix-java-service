package io.osins.matrix.shared.grpc.client;

import io.osins.matrix.shared.tracing.utils.Tracing;
import io.grpc.*;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@RequiredArgsConstructor
public class LoggingClientInterceptor implements ClientInterceptor {

    private final Tracer tracer;         // 注入 Micrometer Tracer
    private final Propagator propagator; // 注入 Micrometer Propagator

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
            MethodDescriptor<ReqT, RespT> method,
            CallOptions callOptions,
            Channel next) {

        log.info("Calling method: {}", method.getFullMethodName());

        var call = next.newCall(method, callOptions);
        return new ForwardingClientCall.SimpleForwardingClientCall<>(call) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                Tracing.tracing(tracer);

                log.info("Request headers: {}", headers);
                log.info("Calling method: {}", method.getFullMethodName());
                super.start(responseListener, headers);
            }
        };
    }
}