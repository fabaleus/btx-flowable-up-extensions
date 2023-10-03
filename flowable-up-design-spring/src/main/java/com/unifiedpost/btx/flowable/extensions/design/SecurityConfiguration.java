/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.design;

import com.flowable.autoconfigure.design.security.DesignHttpSecurityCustomizer;
import com.flowable.design.security.spring.token.FlowableDesignJwtResourceServerConfigurer;
import com.flowable.design.security.spring.web.authentication.AjaxAuthenticationFailureHandler;
import com.flowable.design.security.spring.web.authentication.AjaxAuthenticationSuccessHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.security.web.util.matcher.RequestHeaderRequestMatcher;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfiguration {
	public SecurityConfiguration() {}

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(
			prefix = "application.design.security",
			name = {"type"},
			havingValue = "oauth2")
	public static class OAuth2Configuration {
		@Autowired
		protected ObjectProvider<OidcClientInitiatedLogoutSuccessHandler>
				oidcClientInitiatedLogoutSuccessHandlerProvider;

		public OAuth2Configuration() {}

		@Bean
		@Order(10)
		public SecurityFilterChain oauthDefaultSecurity(
				HttpSecurity http, ObjectProvider<DesignHttpSecurityCustomizer> httpSecurityCustomizers)
				throws Exception {
			Iterator var3 = ((List) httpSecurityCustomizers.orderedStream().collect(Collectors.toList())).iterator();

			while (var3.hasNext()) {
				DesignHttpSecurityCustomizer customizer = (DesignHttpSecurityCustomizer) var3.next();
				customizer.customize(http);
			}

			http.exceptionHandling()
					.defaultAuthenticationEntryPointFor(
							new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
							new RequestHeaderRequestMatcher("X-Requested-With", "XMLHttpRequest"));
			((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
							http.authorizeRequests().anyRequest())
					.authenticated();
			HttpSessionSecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
			securityContextRepository.setDisableUrlRewriting(true);
			http.securityContext().securityContextRepository(securityContextRepository);
			http.oauth2Login();
			http.oauth2Client();
			http.logout().logoutUrl("/auth/logout");
			ObjectProvider var10000 = this.oidcClientInitiatedLogoutSuccessHandlerProvider;
			LogoutConfigurer var10001 = http.logout();
			Objects.requireNonNull(var10001);
			// var10000.ifAvailable(var10001::logoutSuccessHandler);
			ApplicationContext applicationContext = (ApplicationContext) http.getSharedObject(ApplicationContext.class);
			JwtDecoder jwtDecoder = (JwtDecoder)
					applicationContext.getBeanProvider(JwtDecoder.class).getIfAvailable();
			if (jwtDecoder != null && http.getConfigurer(FlowableDesignJwtResourceServerConfigurer.class) == null) {
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
		@Order(10)
		public SecurityFilterChain basicDefaultSecurity(
				HttpSecurity http, ObjectProvider<DesignHttpSecurityCustomizer> httpSecurityCustomizers)
				throws Exception {
			Iterator var3 = ((List) httpSecurityCustomizers.orderedStream().collect(Collectors.toList())).iterator();

			while (var3.hasNext()) {
				DesignHttpSecurityCustomizer customizer = (DesignHttpSecurityCustomizer) var3.next();
				customizer.customize(http);
			}

			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			http.logout().logoutUrl("/auth/logout").logoutSuccessUrl("/");
			((HttpSecurity) ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
									((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
													((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl)
																	((HttpSecurity) ((FormLoginConfigurer)
																							((FormLoginConfigurer)
																											((FormLoginConfigurer)
																															((HttpSecurity)
																																			http.exceptionHandling()
																																					.defaultAuthenticationEntryPointFor(
																																							new HttpStatusEntryPoint(
																																									HttpStatus
																																											.UNAUTHORIZED),
																																							AnyRequestMatcher
																																									.INSTANCE)
																																					.and())
																																	.formLogin()
																																	.loginProcessingUrl(
																																			"/auth/login"))
																													.successHandler(
																															new AjaxAuthenticationSuccessHandler()))
																									.failureHandler(
																											new AjaxAuthenticationFailureHandler()))
																					.and())
																			.authorizeRequests()
																			.antMatchers(new String[] {"/"}))
															.permitAll()
															.antMatchers(new String[] {
																"/**/*.svg",
																"/**/*.ico",
																"/**/*.png",
																"/**/*.woff2",
																"/**/*.css",
																"/**/*.woff",
																"/**/*.html",
																"/**/*.js",
																"/**/flowable-frontend-configuration",
																"/**/index.html"
															}))
											.permitAll()
											.anyRequest())
							.authenticated()
							.and())
					.httpBasic();
			return (SecurityFilterChain) http.build();
		}
	}
}
