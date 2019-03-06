package com.gliesereum.socket.config;

import com.gliesereum.share.common.exception.handler.RestTemplateErrorHandler;
import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.exchange.service.account.impl.UserExchangeServiceImpl;
import com.gliesereum.share.common.exchange.service.karma.KarmaExchangeService;
import com.gliesereum.share.common.exchange.service.karma.impl.KarmaExchangeServiceImpl;
import com.gliesereum.share.common.security.jwt.factory.JwtTokenFactory;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import com.gliesereum.socket.config.interceptor.RestTemplateBuildJwtInterceptor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yurii Vlasiuk
 * @version 1.0
 */

@Configuration
@ComponentScan(basePackageClasses = ExchangeProperties.class)
public class ExchangeConfiguration {

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateBuildJwtInterceptor restTemplateBuildJwtInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> existed = restTemplate.getInterceptors();
        List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors = new ArrayList<>();
        clientHttpRequestInterceptors.add(restTemplateBuildJwtInterceptor);
        if (CollectionUtils.isNotEmpty(existed)) {
            clientHttpRequestInterceptors.addAll(existed);
        }
        restTemplate.setInterceptors(clientHttpRequestInterceptors);
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    @Bean
    public RestTemplateBuildJwtInterceptor restTemplateBuildJwtInterceptor(JwtTokenFactory jwtTokenFactory, SecurityProperties securityProperties) {
        return new RestTemplateBuildJwtInterceptor(jwtTokenFactory, securityProperties);
    }

    @Bean
    public UserExchangeService userExchangeService(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
        return new UserExchangeServiceImpl(restTemplate, exchangeProperties);
    }

    @Bean
    public KarmaExchangeService karmaExchangeService(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
        return new KarmaExchangeServiceImpl(restTemplate, exchangeProperties);
    }
}
