package club.hm.matrix.shared.grpc.client.discovery;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.reactive.SimpleReactiveDiscoveryClient;
import org.springframework.cloud.client.discovery.simple.reactive.SimpleReactiveDiscoveryProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableDiscoveryClient
@EnableAutoConfiguration(exclude = {
        org.springframework.cloud.client.discovery.simple.reactive.SimpleReactiveDiscoveryClientAutoConfiguration.class
})
public class ReactiveGrpcDiscoveryConfiguration {

    /**
     * Kubernetes 环境配置
     */
    @Configuration
    @ConditionalOnProperty(name = "spring.cloud.kubernetes.discovery.enabled", havingValue = "true")
    static class KubernetesReactiveConfig {
        @PostConstruct
        public void init() {
            log.info("Kubernetes reactive gRPC service discovery enabled");
        }

        @Bean
        @ConditionalOnBean(ReactiveDiscoveryClient.class)
        public ReactiveGrpcServiceDiscovery kubernetesReactiveGrpcServiceDiscovery(
                ReactiveDiscoveryClient reactiveDiscoveryClient) {
            log.info("Creating Kubernetes reactive gRPC service discovery with: {}",
                    reactiveDiscoveryClient.getClass().getSimpleName());
            return new ReactiveGrpcServiceDiscovery(reactiveDiscoveryClient);
        }
    }

    /**
     * 本地开发环境配置
     */
    @Configuration
    @ConditionalOnProperty(name = "spring.cloud.kubernetes.discovery.enabled", havingValue = "false", matchIfMissing = true)
    static class LocalReactiveConfig {

        @PostConstruct
        public void init() {
            log.info("Local reactive gRPC service discovery enabled");
        }

        @Bean
        @ConditionalOnMissingBean(ReactiveDiscoveryClient.class)
        public ReactiveDiscoveryClient simpleReactiveDiscoveryClient() {
            log.info("Creating SimpleReactiveDiscoveryClient for local development");
            SimpleReactiveDiscoveryProperties properties = new SimpleReactiveDiscoveryProperties();
            return new SimpleReactiveDiscoveryClient(properties);
        }

        @Bean
        @ConditionalOnMissingBean(ReactiveGrpcServiceDiscovery.class)
        public ReactiveGrpcServiceDiscovery localReactiveGrpcServiceDiscovery(LocalGrpcProperties localGrpcProperties) {
            log.info("Creating local reactive gRPC service discovery");
            return new LocalReactiveGrpcServiceDiscovery(localGrpcProperties);
        }
    }
}