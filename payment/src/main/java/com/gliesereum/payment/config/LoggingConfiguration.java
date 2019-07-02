package com.gliesereum.payment.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 /**
 * @author vitalij
 */
@Configuration
@ComponentScan(value = "com.gliesereum.share.common.logging")
public class LoggingConfiguration {

    private static final String QUEUE_LOGSTASH = "spring.rabbitmq.queue-logstash";

    @Bean
    public Queue queue(Environment environment) {
        return new Queue(environment.getRequiredProperty(QUEUE_LOGSTASH));
    }
}
