/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "flowable.gcp.cloud-storage")
@Component
public class FlowableGcpCloudStorageProperties {

	protected String bucket;

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
}
