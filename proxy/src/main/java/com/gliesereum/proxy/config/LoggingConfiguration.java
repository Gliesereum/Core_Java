package com.gliesereum.proxy.config;

import com.gliesereum.share.common.config.rabbitmq.RabbitMQDefaultConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 04/12/2018
 */
@Configuration
@ComponentScan(
        value = {
                "com.gliesereum.share.common.logging.service",
                "com.gliesereum.share.common.logging.appender"},
        basePackageClasses = RabbitMQDefaultConfiguration.class)
public class LoggingConfiguration {
}
