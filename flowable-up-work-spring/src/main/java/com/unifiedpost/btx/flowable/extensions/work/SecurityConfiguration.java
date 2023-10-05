/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.work;

import com.flowable.autoconfigure.security.FlowableHttpSecurityCustomizer;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	@Order(10)
	public SecurityFilterChain oauthDefaultSecurity(
			HttpSecurity http, ObjectProvider<FlowableHttpSecurityCustomizer> httpSecurityCustomizers)
			throws Exception {
		for (FlowableHttpSecurityCustomizer customizer :
				httpSecurityCustomizers.orderedStream().collect(Collectors.toList())) {
			customizer.customize(http);
		}

		http.exceptionHandling()
				.defaultAuthenticationEntryPointFor(
						new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
						new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));

		// We mark all requests as authenticated in order for the redirect to happen when the application is accessed
		http.authorizeRequests().anyRequest().authenticated();
		// Currently an HttpSessionSecurityContextRepository is needed for the oauth2 to work
		HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
		securityContextRepository.setDisableUrlRewriting(true);
		http.securityContext().securityContextRepository(securityContextRepository);

		http.oauth2Login();
		http.oauth2Client();

		// Remove the line below if Access using Bearer authentication token is not needed
		http.oauth2ResourceServer().jwt();
		ApplicationContext applicationContext = http.getSharedObject(ApplicationContext.class);

		return http.build();
	}
}
