package club.hm.matrix.auth.grpc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"club.hm.matrix"})
@EnableR2dbcRepositories(basePackages = "club.hm.matrix.auth.data")
public class MatrixAuthGrpcProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatrixAuthGrpcProviderApplication.class, args);
    }
}
