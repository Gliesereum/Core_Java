package com.gliesereum.payment.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author vitalij
 */
@Slf4j
@EnableRabbit
@Configuration
public class RabbitMQConfiguration {

    @Bean
    public FanoutExchange orderUpdateInfoExchange(@Value("${liq-pay-exchange.order-update-info.exchange-name}")
                                                            String orderUpdateInfoExchangeName) {
        return new FanoutExchange(orderUpdateInfoExchangeName);
    }
}
