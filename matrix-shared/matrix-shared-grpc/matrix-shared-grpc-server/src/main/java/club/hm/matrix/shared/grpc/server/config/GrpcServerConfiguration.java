package club.hm.matrix.shared.grpc.server.config;


import club.hm.matrix.shared.grpc.server.CustomServerInterceptor;
import club.hm.matrix.shared.grpc.server.ReactiveExceptionHandler;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class GrpcServerConfiguration {

    @Bean
    public NettyChannelBuilder nettyChannelBuilder() {
        return NettyChannelBuilder.forAddress("localhost", 9090)
                .keepAliveTime(30, TimeUnit.SECONDS)
                .keepAliveTimeout(5, TimeUnit.SECONDS)
                .keepAliveWithoutCalls(true)
                .maxInboundMessageSize(4 * 1024 * 1024)
                .usePlaintext();
    }

    // 使用 @GrpcGlobalServerInterceptor 注解自动注册拦截器
    @GrpcGlobalServerInterceptor
    CustomServerInterceptor globalExceptionInterceptor() {
        return new CustomServerInterceptor();
    }
}
