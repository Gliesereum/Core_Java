package com.gliesereum.mail.config;

import com.gliesereum.share.common.config.rabbitmq.RabbitMQDefaultConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Configuration
@Import(RabbitMQDefaultConfiguration.class)
public class RabbitMQConfiguration {
}
