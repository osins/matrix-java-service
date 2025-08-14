package club.hm.matrix.user.grpc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;


@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = "club.hm.matrix")
@EnableR2dbcRepositories(basePackages = "club.hm.matrix.user.data.repository")
public class MatrixUserGrpcProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatrixUserGrpcProviderApplication.class, args);
    }
}
