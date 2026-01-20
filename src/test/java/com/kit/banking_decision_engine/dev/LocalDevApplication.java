package com.kit.banking_decision_engine.dev;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import com.kit.banking_decision_engine.BankingDecisionEngineApplication;

public class LocalDevApplication {
    static void main(String[] args) {
        SpringApplication.from(BankingDecisionEngineApplication::main)
                .with(LocalDevTestcontainersConfig.class)
                .run(args);
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class LocalDevTestcontainersConfig {
        @Bean
        @ServiceConnection
        public PostgreSQLContainer postgresDBContainer() {
            return new PostgreSQLContainer("postgres:16-alpine");
        }
    }
}
