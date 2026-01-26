package com.example.mongoRedis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.micrometer.core.instrument.binder.MeterBinder;

@Configuration
public class MetricsConfig {

    @Bean
    public MeterBinder customMetrics() {
        return registry -> {
            // Placeholder for custom metrics
            // Example: Counter.builder("api.requests").register(registry);
        };
    }
}
