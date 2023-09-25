/* Copyright 2018 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

// @AutoConfigureObservability
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = {"up.prometheus.metrics.prefix=false"},
		classes = {Application.class})
@Slf4j
public class UnprefixedPrometheusTest {

	@LocalManagementPort
	private int managementPort;

	@Autowired
	WebTestClient webTestClient;

	@Test
	public void prometheus_ShouldReturnData() {
		final EntityExchangeResult<String> entity = getManagementEndpoint("/prometheus", String.class);
		assertEquals(HttpStatus.OK.value(), entity.getStatus().value());
		assertTrue(entity.getResponseBody().contains("jvm_threads_live_threads"));
		// make sure no prefix is added
		assertFalse(entity.getResponseBody().contains("_jvm_threads_live_threads"));
	}

	private <T> EntityExchangeResult<T> getManagementEndpoint(final String endpoint, final Class<T> responseType) {
		return this.webTestClient
				.get()
				.uri("http://localhost:" + this.managementPort + endpoint)
				.exchange()
				.expectBody(responseType)
				.returnResult();
	}
}
