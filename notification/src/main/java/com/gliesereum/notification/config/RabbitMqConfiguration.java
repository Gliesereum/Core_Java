package com.gliesereum.notification.config;

import com.gliesereum.share.common.config.rabbitmq.RabbitMQDefaultConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Configuration
@Import(RabbitMQDefaultConfiguration.class)
public class RabbitMqConfiguration {
}
