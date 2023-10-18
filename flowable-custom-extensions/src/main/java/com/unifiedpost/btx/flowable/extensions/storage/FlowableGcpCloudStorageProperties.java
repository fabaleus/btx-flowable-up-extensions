/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "flowable.gcp.cloud-storage")
public class FlowableGcpCloudStorageProperties {

	protected String bucket;

	public String getBucket() {
		return bucket;
	}

	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
}
