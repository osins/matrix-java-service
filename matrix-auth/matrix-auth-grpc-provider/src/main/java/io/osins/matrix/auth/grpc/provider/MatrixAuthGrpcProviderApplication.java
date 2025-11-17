package io.osins.matrix.auth.grpc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"io.osins.matrix"})
@EnableR2dbcRepositories(basePackages = "io.osins.matrix.auth.data")
public class MatrixAuthGrpcProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(MatrixAuthGrpcProviderApplication.class, args);
    }
}
