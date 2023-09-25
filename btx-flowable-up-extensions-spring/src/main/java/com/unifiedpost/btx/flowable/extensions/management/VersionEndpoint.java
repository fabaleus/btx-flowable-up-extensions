/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.management;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

/**
 * Endpoint that provides information about the current application version.
 */
@Component
@Endpoint(id = "version")
public class VersionEndpoint {

	@Value("${info.app.version}")
	private String version;

	@ReadOperation
	public String invoke() {
		return version;
	}
}
