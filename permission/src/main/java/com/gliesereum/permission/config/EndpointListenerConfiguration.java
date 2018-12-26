package com.gliesereum.permission.config;

import com.gliesereum.permission.service.endpoint.EndpointService;
import com.gliesereum.share.common.config.migration.EndpointListenerDefaultConfiguration;
import com.gliesereum.share.common.exchange.service.permission.EndpointExchangeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-26
 */
@Configuration
@ComponentScan(basePackageClasses = EndpointListenerDefaultConfiguration.class)
public class EndpointListenerConfiguration {

    @Bean
    public EndpointExchangeService endpointExchangeService(EndpointService endpointService) {
        return endpointService::createPack;
    }
}
