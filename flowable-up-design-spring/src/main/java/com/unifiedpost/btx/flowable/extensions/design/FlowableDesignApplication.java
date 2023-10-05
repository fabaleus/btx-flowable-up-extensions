/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.design;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@SpringBootApplication(exclude = {FreeMarkerAutoConfiguration.class})
public class FlowableDesignApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowableDesignApplication.class, args);
	}
}
