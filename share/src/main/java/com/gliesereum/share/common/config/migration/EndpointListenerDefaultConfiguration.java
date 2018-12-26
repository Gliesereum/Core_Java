package com.gliesereum.share.common.config.migration;

import com.gliesereum.share.common.config.executor.ThreadPoolTaskExecutorDefaultConfiguration;
import com.gliesereum.share.common.exception.handler.RestTemplateErrorHandler;
import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.permission.EndpointExchangeService;
import com.gliesereum.share.common.exchange.service.permission.impl.EndpointExchangeServiceImpl;
import com.gliesereum.share.common.migration.EndpointsListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-26
 */

@Configuration
@EnableConfigurationProperties(ExchangeProperties.class)
@ComponentScan(basePackageClasses = {
        EndpointsListener.class,
        ThreadPoolTaskExecutorDefaultConfiguration.class
})
public class EndpointListenerDefaultConfiguration {

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public EndpointExchangeService endpointExchangeService(ExchangeProperties exchangeProperties, RestTemplate restTemplate) {
        return new EndpointExchangeServiceImpl(restTemplate, exchangeProperties);
    }
}
