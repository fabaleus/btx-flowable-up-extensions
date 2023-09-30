/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.design;

import com.flowable.design.actuate.autoconfigure.security.servlet.ActuatorRequestMatcher;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@ConditionalOnClass({EndpointRequest.class})
@Configuration(proxyBeanMethods = false)
public class SecurityActuatorConfiguration {
	public SecurityActuatorConfiguration() {}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(
			prefix = "application.design.security",
			name = {"type"},
			havingValue = "oauth2")
	public static class OAuth2Configuration {
		public OAuth2Configuration() {}

		@Bean
		@Order(6)
		public SecurityFilterChain oauthActuatorSecurity(HttpSecurity http) throws Exception {
			((HttpSecurity) http.sessionManagement()
							.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
							.and())
					.csrf()
					.disable();
			((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
									((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) http.requestMatcher(
															new ActuatorRequestMatcher())
													.authorizeRequests()
													.requestMatchers(new RequestMatcher[] {
														EndpointRequest.to(
																new Class[] {InfoEndpoint.class, HealthEndpoint.class})
													}))
											.permitAll()
											.requestMatchers(new RequestMatcher[] {EndpointRequest.toAnyEndpoint()}))
							.hasAuthority("access-admin")
							.anyRequest())
					.denyAll();
			http.oauth2Login();
			http.oauth2Client();
			ApplicationContext applicationContext = (ApplicationContext) http.getSharedObject(ApplicationContext.class);
			JwtDecoder jwtDecoder = (JwtDecoder)
					applicationContext.getBeanProvider(JwtDecoder.class).getIfAvailable();
			if (jwtDecoder != null) {
				http.oauth2ResourceServer().jwt();
			}

			return (SecurityFilterChain) http.build();
		}
	}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(
			prefix = "application.design.security",
			name = {"type"},
			havingValue = "basic",
			matchIfMissing = true)
	public static class HttpBasicConfiguration {
		public HttpBasicConfiguration() {}

		@Bean
		@Order(6)
		public SecurityFilterChain basicActuatorSecurity(HttpSecurity http) throws Exception {
			((HttpSecurity) http.sessionManagement()
							.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
							.and())
					.csrf()
					.disable();
			((HttpSecurity) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
									((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
													((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
																	http.requestMatcher(new ActuatorRequestMatcher())
																			.authorizeRequests()
																			.requestMatchers(new RequestMatcher[] {
																				EndpointRequest.to(new Class[] {
																					InfoEndpoint.class,
																					HealthEndpoint.class
																				})
																			}))
															.permitAll()
															.requestMatchers(new RequestMatcher[] {
																EndpointRequest.toAnyEndpoint()
															}))
											.hasAuthority("access-admin")
											.anyRequest())
							.denyAll()
							.and())
					.httpBasic();
			return (SecurityFilterChain) http.build();
		}
	}
}
