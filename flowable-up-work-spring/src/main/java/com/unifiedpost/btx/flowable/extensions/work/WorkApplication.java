/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.work;

import com.unifiedpost.btx.flowable.extensions.storage.FlowableGcpCloudStoragePackageMarker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackageClasses = {FlowableGcpCloudStoragePackageMarker.class, FlowableWorkPackageMarker.class})
@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
public class WorkApplication {
	public WorkApplication() {}

	public static void main(String[] args) {
		SpringApplication.run(WorkApplication.class, args);
	}
}
