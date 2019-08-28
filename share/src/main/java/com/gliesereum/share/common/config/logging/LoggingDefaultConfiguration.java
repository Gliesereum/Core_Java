package com.gliesereum.share.common.config.logging;

import com.gliesereum.share.common.config.rabbitmq.RabbitMQDefaultConfiguration;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Configuration
@ComponentScan(
        value = "com.gliesereum.share.common.logging",
        basePackageClasses = RabbitMQDefaultConfiguration.class)
public class LoggingDefaultConfiguration {
}
