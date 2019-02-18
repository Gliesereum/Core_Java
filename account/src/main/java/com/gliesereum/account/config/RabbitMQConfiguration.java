package com.gliesereum.account.config;

import com.gliesereum.share.common.config.rabbitmq.RabbitMQDefaultConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@ComponentScan(basePackageClasses = RabbitMQDefaultConfiguration.class)
@Configuration
public class RabbitMQConfiguration {

    private final String MAIL_QUEUE = "spring.rabbitmq.queue-mail";

    @Bean
    public Queue queue(Environment environment) {
        return new Queue(environment.getRequiredProperty(MAIL_QUEUE));
    }

}
