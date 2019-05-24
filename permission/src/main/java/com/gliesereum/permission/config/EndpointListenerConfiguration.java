package com.gliesereum.permission.config;

import com.gliesereum.permission.service.endpoint.EndpointService;
import com.gliesereum.share.common.config.executor.ThreadPoolTaskExecutorDefaultConfiguration;
import com.gliesereum.share.common.config.migration.EndpointListenerDefaultConfiguration;
import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.permission.EndpointExchangeService;
import com.gliesereum.share.common.exchange.service.permission.impl.EndpointExchangeServiceImpl;
import com.gliesereum.share.common.migration.EndpointsListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Configuration
@EnableConfigurationProperties(ExchangeProperties.class)
@ComponentScan(basePackageClasses = {
        EndpointsListener.class,
        ThreadPoolTaskExecutorDefaultConfiguration.class
})
public class EndpointListenerConfiguration {

    @Bean
    public EndpointExchangeService endpointExchangeService(EndpointService endpointService) {
        return endpointService::createPack;
    }
}
