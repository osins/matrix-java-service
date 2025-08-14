package club.hm.matrix.user.grpc.consumer.test;

import club.hm.matrix.shared.grpc.client.discovery.LocalReactiveGrpcServiceDiscovery;
import club.hm.matrix.user.grpc.consumer.service.UserGrpcClientService;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import reactor.core.publisher.Mono;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "club.hm.matrix")
public class TestConfig {

}
