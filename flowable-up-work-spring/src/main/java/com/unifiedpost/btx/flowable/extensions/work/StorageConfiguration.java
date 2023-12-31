/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.work;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.unifiedpost.btx.flowable.extensions.storage.FlowableGcpCloudStorageProperties;
import com.unifiedpost.btx.flowable.extensions.storage.GcpContentStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfiguration {

	@Bean
	public GcpContentStorage gcpContentStorage(Storage storage, FlowableGcpCloudStorageProperties properties) {
		return new GcpContentStorage(storage, properties.getBucket());
	}

	@Bean
	public Storage gcpStorage() {
		return StorageOptions.getDefaultInstance().getService();
	}
}
