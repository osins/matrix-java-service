package osins.matrix.shared.grpc.server.config;


import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import net.devh.boot.grpc.server.serverfactory.GrpcServerConfigurer;
import osins.matrix.shared.grpc.server.CustomServerInterceptor;
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

    /**
     * 服务端配置 gRPC 参数
     */
    @Bean
    public GrpcServerConfigurer grpcServerConfigurer() {
        return serverBuilder -> {
            if (serverBuilder instanceof NettyServerBuilder builder) {
                builder
                        .keepAliveTime(30, TimeUnit.SECONDS)
                        .keepAliveTimeout(5, TimeUnit.SECONDS)
                        .maxInboundMessageSize(4 * 1024 * 1024)
                        .maxConcurrentCallsPerConnection(100); // 每个连接允许的最大流数（多路复用能力）
            }
        };
    }

    // 使用 @GrpcGlobalServerInterceptor 注解自动注册拦截器
    @GrpcGlobalServerInterceptor
    CustomServerInterceptor globalExceptionInterceptor() {
        return new CustomServerInterceptor();
    }
}
