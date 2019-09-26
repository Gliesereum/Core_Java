package com.gliesereum.payment.config;

import com.gliesereum.share.common.config.rabbitmq.RabbitMQDefaultConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author vitalij
 */
@Slf4j
@Configuration
@ComponentScan(basePackageClasses = RabbitMQDefaultConfiguration.class)
public class RabbitMQConfiguration {

   /* @Bean
    public FanoutExchange orderUpdateStatusPaymentInfoExchange(@Value("${way-for-pay.exchange.order-update-status-payment-info.exchange-name}")
                                                            String orderUpdateStatusPaymentInfoExchangeName) {
        return new FanoutExchange(orderUpdateStatusPaymentInfoExchangeName);
    }

    @Bean
    public FanoutExchange businessUpdateTypePaymentInfoExchange(@Value("${way-for-pay.exchange.business-update-type-payment-info.exchange-name}")
                                                                       String businessUpdateTypePaymentInfoExchangeName) {
        return new FanoutExchange(businessUpdateTypePaymentInfoExchangeName);
    }*/
}
