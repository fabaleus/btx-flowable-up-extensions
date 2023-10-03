/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.work;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
	public ServletInitializer() {}

	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(new Class[] {WorkApplication.class});
	}
}
