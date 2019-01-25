package com.gliesereum.share.common.config.logging;

import com.gliesereum.share.common.config.rabbitmq.RabbitMQDefaultConfiguration;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private Environment environment;

    private final String QUEUE_LOGSTASH = "spring.rabbitmq.queue-logstash";

    @Bean
    public Queue queue() {
        return new Queue(environment.getRequiredProperty(QUEUE_LOGSTASH));
    }
}
