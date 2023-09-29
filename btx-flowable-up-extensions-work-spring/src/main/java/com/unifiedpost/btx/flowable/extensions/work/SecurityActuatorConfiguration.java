/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.work;

import com.flowable.actuate.autoconfigure.security.servlet.ActuatorRequestMatcher;
import com.flowable.platform.common.security.SecurityConstants;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityActuatorConfiguration {

	@Bean
	@Order(6) // Actuator configuration should kick in before the Form Login there should always be HTTP basic for the
	// endpoints
	public SecurityFilterChain basicActuatorSecurity(HttpSecurity http) throws Exception {

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//				.and()
		//				.csrf()
		//				.disable(); //todo: check impact

		http.requestMatcher(new ActuatorRequestMatcher())
				.authorizeRequests()
				.requestMatchers(EndpointRequest.to(InfoEndpoint.class, HealthEndpoint.class))
				.permitAll()
				.requestMatchers(EndpointRequest.toAnyEndpoint())
				.hasAuthority(SecurityConstants.ACCESS_ACTUATORS)
				.anyRequest()
				.denyAll()
				.and()
				.httpBasic();

		return http.build();
	}
}
