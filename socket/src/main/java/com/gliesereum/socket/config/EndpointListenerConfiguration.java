package com.gliesereum.socket.config;

import com.gliesereum.share.common.config.migration.EndpointListenerDefaultConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yurii Vlasiuk
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackageClasses = EndpointListenerDefaultConfiguration.class)
public class EndpointListenerConfiguration {
}
