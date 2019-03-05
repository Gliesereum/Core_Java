package com.gliesereum.socket.config;

import com.gliesereum.share.common.config.logging.LoggingDefaultConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Yurii Vlasiuk
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackageClasses = LoggingDefaultConfiguration.class)
public class LoggingConfiguration {
}
