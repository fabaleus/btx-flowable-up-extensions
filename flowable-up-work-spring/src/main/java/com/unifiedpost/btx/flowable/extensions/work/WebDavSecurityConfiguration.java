/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.work;

import com.flowable.autoconfigure.webdav.FlowableWebDavProperties;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.flowable.common.engine.api.FlowableException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@ConditionalOnProperty(
		prefix = "flowable.webdav",
		name = {"enabled"},
		havingValue = "true",
		matchIfMissing = false)
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({FlowableWebDavProperties.class})
public class WebDavSecurityConfiguration {
	private static final String WEBDAV_URL = "/platform-api/webdav/**";
	protected final FlowableWebDavProperties webDavProperties;

	public WebDavSecurityConfiguration(FlowableWebDavProperties webDavProperties) {
		this.webDavProperties = webDavProperties;
	}

	@Bean
	@Order(7)
	@SuppressFBWarnings(value = "SPRING_CSRF_PROTECTION_DISABLED", justification = "This resource is stateless.")
	public SecurityFilterChain webDavSecurity(HttpSecurity http) throws Exception {
		if ("basic-auth".equals(this.webDavProperties.getAuthenticationMode())) {
			((HttpSecurity) ((HttpSecurity) ((HttpSecurity) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
													http.antMatcher("/platform-api/webdav/**")
															.authorizeRequests()
															.anyRequest())
											.authenticated()
											.and())
									.httpBasic()
									.authenticationEntryPoint((request, response, authException) -> {
										response.addHeader("WWW-Authenticate", "Basic realm=\"FLOWABLE_WEBDAV\"");
										response.sendError(401, authException.getMessage());
									})
									.and())
							.sessionManagement()
							.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
							.and())
					.csrf()
					.disable();
		} else {
			if (!"none".equals(this.webDavProperties.getAuthenticationMode())) {
				throw new FlowableException(
						"Unknown WEBDAV authentication mode: " + this.webDavProperties.getAuthenticationMode());
			}

			((HttpSecurity) ((HttpSecurity) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
											http.antMatcher("/platform-api/webdav/**")
													.authorizeRequests()
													.anyRequest())
									.permitAll()
									.and())
							.sessionManagement()
							.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
							.and())
					.csrf()
					.disable();
		}

		return (SecurityFilterChain) http.build();
	}

	@Bean
	@Order(7)
	public WebSecurityCustomizer webDavWebSecurityCustomizer() {
		return (web) -> {
			StrictHttpFirewall strictHttpFirewall = new StrictHttpFirewall();
			strictHttpFirewall.setUnsafeAllowAnyHttpMethod(true);
			web.httpFirewall(strictHttpFirewall);
		};
	}
}
