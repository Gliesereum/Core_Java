package com.gliesereum.account.config;

import com.gliesereum.share.common.config.rabbitmq.RabbitMQDefaultConfiguration;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author vitalij
 * @version 1.0
 */
@ComponentScan(basePackageClasses = RabbitMQDefaultConfiguration.class)
@Configuration
public class RabbitMQConfiguration {

    @Bean
    public FanoutExchange corporationDeleteExchange(
            @Value("${system-notification.corporation-delete.exchange-name}")
                    String corporationDeleteExchangeName) {
        return new FanoutExchange(corporationDeleteExchangeName);
    }
}
