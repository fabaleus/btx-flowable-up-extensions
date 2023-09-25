/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.controller;

import com.unifiedpost.btx.flowable.extensions.api.Api;
import com.unifiedpost.btx.flowable.extensions.model.HelloWorld;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Slf4j
@AllArgsConstructor
public class ApiController implements Api {

	private final MeterRegistry registry;

	@Override
	@Timed("example.timer")
	public Mono<HelloWorld> helloWorld() {
		return Mono.just(HelloWorld.builder().text("Hello World!").build())
				.doOnError(throwable -> log.error("Error log on error.", throwable))
				.doOnSuccess(s -> log.info("Info on success."))
				.name("example.reactive");
	}
}
