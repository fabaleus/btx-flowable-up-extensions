/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.api;

import com.unifiedpost.btx.flowable.extensions.model.HelloWorld;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

public interface Api {

	@ApiResponses(
			value = {
				@ApiResponse(
						responseCode = "200",
						description = "hello!",
						content = {
							@Content(
									mediaType = "application/json",
									schema = @Schema(implementation = HelloWorld.class))
						})
			})
	@GetMapping(path = "/hello")
	public Mono<HelloWorld> helloWorld();
}
