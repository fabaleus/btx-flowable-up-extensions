/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions;

import com.unifiedpost.btx.flowable.extensions.controller.ApiController;
import com.unifiedpost.btx.flowable.extensions.model.HelloWorld;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

@SpringBootTest
@ActiveProfiles("test")
public class ApplicationTest {
	@Autowired
	private ApiController apiController;

	@Test
	public void helloWorldApiTest() {
		StepVerifier.create(apiController.helloWorld())
				.expectNext(HelloWorld.builder().text("Hello World!").build())
				.verifyComplete();
	}
}
