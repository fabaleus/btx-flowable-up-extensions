/* Copyright 2018 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.test.autoconfigure.actuate.observability.AutoConfigureObservability;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalManagementPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

// @AutoConfigureObservability
@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		classes = {Application.class})
@Slf4j
public class ManagementEndpointTest {

	@LocalManagementPort
	private int managementPort;

	@Autowired
	private WebTestClient webTestClient;

	@Value("${info.app.buildTimestamp}")
	private String buildTimestamp;

	@Value("${info.app.version}")
	private String version;

	@Test
	public void version_ShouldReturnVersion() {
		EntityExchangeResult<String> entity = getManagementEndpoint("/version", String.class);
		assertEquals(HttpStatus.OK.value(), entity.getStatus().value());
		assertEquals(version, entity.getResponseBody());
	}

	@Test
	public void metrics_ShouldReturnJsonResponse() throws JSONException {
		EntityExchangeResult<String> entity = getManagementEndpoint("/metrics", String.class);
		assertEquals(HttpStatus.OK.value(), entity.getStatus().value());
		log.info(entity.getResponseBody());
		JSONAssert.assertEquals(
				"['jvm.memory.used']",
				JsonPath.read(entity.getResponseBody(), "$.names[?(@ == 'jvm.memory.used')]")
						.toString(),
				JSONCompareMode.STRICT);

		entity = getManagementEndpoint("/metrics/jvm.memory.used", String.class);
		assertEquals(HttpStatus.OK.value(), entity.getStatus().value());
		log.info(entity.getResponseBody());
		JSONAssert.assertEquals(
				"{'name':'jvm.memory.used', 'description': 'The amount of used memory' }",
				entity.getResponseBody(),
				JSONCompareMode.LENIENT);
	}

	@Test
	public void info_ShouldReturnJsonResponse() throws JSONException {
		EntityExchangeResult<String> entity = getManagementEndpoint("/info", String.class);
		assertEquals(HttpStatus.OK.value(), entity.getStatus().value());
		log.info(entity.getResponseBody());
		JSONAssert.assertEquals(
				"{'app':{ 'buildTimestamp':'" + buildTimestamp + "'}}",
				entity.getResponseBody(),
				JSONCompareMode.LENIENT);
	}

	@Test
	public void health_ShouldReturnJsonResponse() throws JSONException {
		EntityExchangeResult<String> entity = getManagementEndpoint("/health", String.class);
		assertEquals(HttpStatus.OK.value(), entity.getStatus().value());
		log.info(entity.getResponseBody());
		JSONAssert.assertEquals("{'status':'UP'}", entity.getResponseBody(), JSONCompareMode.LENIENT);
	}

	@Test
	public void prometheus_ShouldReturnData() {
		EntityExchangeResult<String> entity = getManagementEndpoint("/prometheus", String.class);
		assertEquals(HttpStatus.OK.value(), entity.getStatus().value());
		assertTrue(entity.getResponseBody().contains("service_jvm_threads_live_threads"));
		log.info(entity.getResponseBody());
	}

	private <T> EntityExchangeResult<T> getManagementEndpoint(String endpoint, Class<T> responseType) {
		return this.webTestClient
				.get()
				.uri("http://localhost:" + this.managementPort + endpoint)
				.exchange()
				.expectBody(responseType)
				.returnResult();
	}
}
