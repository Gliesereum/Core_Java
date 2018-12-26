package com.gliesereum.account.config;

import com.gliesereum.share.common.config.migration.EndpointListenerDefaultConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-26
 */
@Configuration
@ComponentScan(basePackageClasses = EndpointListenerDefaultConfiguration.class)
public class EndpointListenerConfiguration {
}
