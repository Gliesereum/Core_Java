package com.gliesereum.media.config;

import com.gliesereum.share.common.config.logging.LoggingDefaultConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackageClasses = LoggingDefaultConfiguration.class)
public class LoggingConfiguration {
}
