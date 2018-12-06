package com.gliesereum.account.config;

import com.gliesereum.share.common.exception.handler.RestTemplateErrorHandler;
import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.exchange.service.account.impl.UserExchangeServiceImpl;
import com.gliesereum.share.common.exchange.service.permission.EndpointExchangeService;
import com.gliesereum.share.common.exchange.service.permission.impl.EndpointExchangeServiceImpl;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 05/12/2018
 */

@Configuration
@ComponentScan(basePackageClasses = ExchangeProperties.class)
public class ExchangeConfiguration {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    //TODO: to endpoint migrate
//    @Bean
//    public EndpointExchangeService endpointExchangeService(ExchangeProperties exchangeProperties, RestTemplate restTemplate) {
//        return new EndpointExchangeServiceImpl(restTemplate, exchangeProperties);
//    }
}
