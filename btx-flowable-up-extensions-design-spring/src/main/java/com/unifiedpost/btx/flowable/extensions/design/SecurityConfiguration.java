/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.design;

import com.flowable.autoconfigure.design.security.DesignHttpSecurityCustomizer;
import com.flowable.core.spring.security.web.authentication.AjaxAuthenticationFailureHandler;
import com.flowable.core.spring.security.web.authentication.AjaxAuthenticationSuccessHandler;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	@Order(10)
	public SecurityFilterChain basicDefaultSecurity(
			HttpSecurity http, ObjectProvider<DesignHttpSecurityCustomizer> httpSecurityCustomizers) throws Exception {
		for (DesignHttpSecurityCustomizer customizer :
				httpSecurityCustomizers.orderedStream().collect(Collectors.toList())) {
			customizer.customize(http);
		}

		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.logout().logoutUrl("/auth/logout").logoutSuccessUrl("/");

		http.exceptionHandling()
				.defaultAuthenticationEntryPointFor(
						new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED), AnyRequestMatcher.INSTANCE)
				.and()
				.formLogin()
				.loginProcessingUrl("/auth/login")
				.successHandler(new AjaxAuthenticationSuccessHandler())
				.failureHandler(new AjaxAuthenticationFailureHandler())
				.and()
				.authorizeRequests()
				.antMatchers("/")
				.permitAll()
				.antMatchers(
						"/**/*.svg",
						"/**/*.ico",
						"/**/*.png",
						"/**/*.woff2",
						"/**/*.css",
						"/**/*.woff",
						"/**/*.html",
						"/**/*.js",
						"/**/flowable-frontend-configuration",
						"/**/index.html")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and()
				.httpBasic();

		return http.build();
	}
}
