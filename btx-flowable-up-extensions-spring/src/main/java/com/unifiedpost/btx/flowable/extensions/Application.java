/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@SpringBootApplication(
		scanBasePackages = {"com.unifiedpost.btx.flowable.extensions.*", "com.unifiedpost.btx.shared.monitoring"},
		exclude = {FreeMarkerAutoConfiguration.class})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
