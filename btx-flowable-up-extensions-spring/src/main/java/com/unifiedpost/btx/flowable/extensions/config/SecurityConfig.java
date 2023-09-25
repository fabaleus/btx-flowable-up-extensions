/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerReactiveAuthenticationManagerResolver;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {
	@Value("#{${up.security.jwt.issuers:''}}")
	private List<String> jwtIssuers;

	@Bean
	SecurityWebFilterChain springSecurityFilterChain(final ServerHttpSecurity http) {
		if (jwtIssuers.isEmpty()) {
			return http.build();
		}

		final var authenticationManagerResolver = new JwtIssuerReactiveAuthenticationManagerResolver(jwtIssuers);
		http.anonymous()
				.and()
				.authorizeExchange((authorize) -> authorize.anyExchange().authenticated())
				.oauth2ResourceServer((resourceServer) ->
						resourceServer.authenticationManagerResolver(authenticationManagerResolver));
		return http.build();
	}
}
