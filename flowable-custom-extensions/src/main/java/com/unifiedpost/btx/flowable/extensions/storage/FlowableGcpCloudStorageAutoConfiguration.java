package com.unifiedpost.btx.flowable.extensions.storage;

import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties({FlowableGcpCloudStorageProperties.class})
// The GCP Spring integration automatically configures and registeres a Storage bean in the Spring application context
@ConditionalOnClass({Storage.class})
public class FlowableGcpCloudStorageAutoConfiguration {

}
