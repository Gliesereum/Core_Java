package com.gliesereum.account.config;

import com.gliesereum.share.common.config.logging.LoggingDefaultConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 04/12/2018
 */
@Configuration
@ComponentScan(basePackageClasses = LoggingDefaultConfiguration.class)
public class LoggingConfiguration {
}
