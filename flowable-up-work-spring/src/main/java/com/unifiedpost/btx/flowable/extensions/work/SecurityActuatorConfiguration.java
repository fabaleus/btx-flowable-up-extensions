/* Copyright 2023 Unifiedpost SA. */
package com.unifiedpost.btx.flowable.extensions.work;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

@ConditionalOnClass({EndpointRequest.class})
@Configuration(proxyBeanMethods = false)
public class SecurityActuatorConfiguration {
	public SecurityActuatorConfiguration() {}
}
