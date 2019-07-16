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
    public FanoutExchange corporationDeleteExchange(@Value("${system-notification.corporation-delete.exchange-name}")
                                                            String corporationDeleteExchangeName) {
        return new FanoutExchange(corporationDeleteExchangeName);
    }

    @Bean
    public FanoutExchange signupWithCodeExchange(@Value("${system-notification.signup-with-code.exchange-name}")
                                                         String signupWithCodeExchangeName) {
        return new FanoutExchange(signupWithCodeExchangeName);
    }

    @Bean
    public FanoutExchange updateAuthInfoExchange(@Value("${system-notification.update-auth-info.exchange-name}")
                                                         String updateAuthInfoExchangeName) {
        return new FanoutExchange(updateAuthInfoExchangeName);
    }

    @Bean
    public FanoutExchange updateClientInfoExchange(@Value("${system-notification.update-client-info.exchange-name}")
                                                         String updateClientInfoExchange) {
        return new FanoutExchange(updateClientInfoExchange);
    }
}
