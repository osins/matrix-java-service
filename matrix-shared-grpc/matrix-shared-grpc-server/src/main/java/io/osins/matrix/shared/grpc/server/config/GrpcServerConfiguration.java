package io.osins.matrix.shared.grpc.server.config;


import io.osins.matrix.shared.grpc.server.CustomServerInterceptor;
import io.grpc.netty.shaded.io.grpc.netty.NettyChannelBuilder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(GrpcServerConfiguration.GrpcNettyProperties.class)
public class GrpcServerConfiguration {
    private final GrpcNettyProperties grpcNettyProperties;

    @Bean
    public NettyChannelBuilder nettyChannelBuilder() {
        return NettyChannelBuilder.forAddress(grpcNettyProperties.getHost(), 9090)
                .keepAliveTime(grpcNettyProperties.getKeepAliveTime(), TimeUnit.SECONDS)
                .keepAliveTimeout(grpcNettyProperties.getKeepAliveTimeout(), TimeUnit.SECONDS)
                .keepAliveWithoutCalls(grpcNettyProperties.isKeepAliveWithoutCalls())
                .maxInboundMessageSize(grpcNettyProperties.getMaxInboundMessageSize())
                .usePlaintext();
    }

    // 使用 @GrpcGlobalServerInterceptor 注解自动注册拦截器
    @GrpcGlobalServerInterceptor
    CustomServerInterceptor globalExceptionInterceptor() {
        return new CustomServerInterceptor();
    }

    @Data
    @ConfigurationProperties(prefix = "grpc.server")
    public static class GrpcNettyProperties {
        private String host = "localhost";
        private int port = 9090;
        private long keepAliveTime = 30;
        private long keepAliveTimeout = 5;
        private boolean keepAliveWithoutCalls = true;
        private int maxInboundMessageSize = 4 * 1024 * 1024;
    }
}
