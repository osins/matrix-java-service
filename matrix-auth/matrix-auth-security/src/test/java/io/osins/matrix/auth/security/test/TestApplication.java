package io.osins.matrix.auth.security.test;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.osins.matrix")
public class TestApplication {
    public static void main(String[] args) {
        org.springframework.boot.SpringApplication.run(TestApplication.class, args);
    }
}
