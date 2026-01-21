package com.kit.banking_decision_engine.dev;

import com.kit.banking_decision_engine.config.TestContainerConfig;
import org.springframework.boot.SpringApplication;
import com.kit.banking_decision_engine.BankingDecisionEngineApplication;

public class LocalDevApplication {
    static void main(String[] args) {
        SpringApplication.from(BankingDecisionEngineApplication::main)
                .with(TestContainerConfig.class)
                .run(args);
    }
}
