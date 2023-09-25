/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonitoringConfig {

	@Bean
	public TimedAspect timedAspect(MeterRegistry registry) {
		// Aspect needed to handle @Timed annotations, see ApiController for example
		return new TimedAspect(registry);
	}

	@Bean
	public MeterBinder helloApiCounter() {
		return (registry) -> {
			Gauge.builder("example.gauge", MonitoringConfig::exampleGauge)
					.description("example gauge metric")
					.register(registry);
		};
	}

	public static int exampleGauge() {
		return 42;
	}
}
