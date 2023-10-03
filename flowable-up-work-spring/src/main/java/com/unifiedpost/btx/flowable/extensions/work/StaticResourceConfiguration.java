/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.work;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class StaticResourceConfiguration implements WebMvcConfigurer {
	public StaticResourceConfiguration() {}

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.setOrder(10)
				.addResourceHandler(new String[] {"/ext/*.js", "/*/ext/*.js"})
				.addResourceLocations(new String[] {"classpath:/static/ext/", "classpath:/public/ext/"})
				.setCacheControl(CacheControl.noCache());
		registry.setOrder(20)
				.addResourceHandler(new String[] {"/ext/*.css", "/*/ext/*.css"})
				.addResourceLocations(new String[] {"classpath:/static/ext/", "classpath:/public/ext/"})
				.setCacheControl(CacheControl.noCache());
		registry.setOrder(30)
				.addResourceHandler(new String[] {"/*.js"})
				.addResourceLocations(new String[] {"classpath:/public/"})
				.setCacheControl(CacheControl.maxAge(365L, TimeUnit.DAYS));
		registry.setOrder(40)
				.addResourceHandler(new String[] {"/*.css"})
				.addResourceLocations(new String[] {"classpath:/public/"})
				.setCacheControl(CacheControl.maxAge(365L, TimeUnit.DAYS));
		registry.setOrder(50)
				.addResourceHandler(new String[] {"/*.woff"})
				.addResourceLocations(new String[] {"classpath:/public/"})
				.setCacheControl(CacheControl.maxAge(365L, TimeUnit.DAYS));
		registry.setOrder(60)
				.addResourceHandler(new String[] {"/*.woff2"})
				.addResourceLocations(new String[] {"classpath:/public/"})
				.setCacheControl(CacheControl.maxAge(365L, TimeUnit.DAYS));
		registry.setOrder(70)
				.addResourceHandler(new String[] {"/*.svg"})
				.addResourceLocations(new String[] {"classpath:/public/", "classpath:/public/twemoji/"})
				.setCacheControl(CacheControl.maxAge(365L, TimeUnit.DAYS));
		registry.setOrder(80)
				.addResourceHandler(new String[] {"/*.map"})
				.addResourceLocations(new String[] {"classpath:/public/"})
				.setCacheControl(CacheControl.maxAge(365L, TimeUnit.DAYS));
		registry.setOrder(90)
				.addResourceHandler(new String[] {"/*.png"})
				.addResourceLocations(new String[] {"classpath:/public/"})
				.setCacheControl(CacheControl.maxAge(365L, TimeUnit.DAYS));
		registry.setOrder(100)
				.addResourceHandler(new String[] {"/*.ico"})
				.addResourceLocations(new String[] {"classpath:/public/"})
				.setCacheControl(CacheControl.maxAge(365L, TimeUnit.DAYS));
	}
}
