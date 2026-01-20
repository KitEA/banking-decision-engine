package com.kit.banking_decision_engine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.BodyFilter;

import java.util.regex.Pattern;

@Configuration
public class LogbookConfig {

    private static final Pattern PERSONAL_CODE_JSON =
            Pattern.compile("(\"personalCode\"\\s*:\\s*\")([^\"]+)(\")");

    @Bean
    public BodyFilter personalCodeMaskingBodyFilter() {
        return (contentType, body) ->
                PERSONAL_CODE_JSON.matcher(body)
                        .replaceAll("$1***$3");
    }
}
