package com.unifiedpost.btx.flowable.extensions.work;

import com.unifiedpost.btx.flowable.extensions.secrets.SecretsResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecretsConfiguration {

    @Bean
    public SecretsResolver secretsResolver() {
        return new SecretsResolver();
    }
}
