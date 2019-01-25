package com.gliesereum.karma.config;

import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.converter.imp.DefaultConverterImp;
import com.gliesereum.share.common.exception.handler.RestTemplateErrorHandler;
import com.gliesereum.share.common.exchange.interceptor.RestTemplateAuthorizationInterceptor;
import com.gliesereum.share.common.security.jwt.properties.JwtSecurityProperties;
import org.apache.commons.collections4.CollectionUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
@Configuration
public class BeanConfiguration {

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateAuthorizationInterceptor restTemplateAuthorizationInterceptor) {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> existed = restTemplate.getInterceptors();
        List<ClientHttpRequestInterceptor> clientHttpRequestInterceptors = new ArrayList<>();
        clientHttpRequestInterceptors.add(restTemplateAuthorizationInterceptor);
        if (CollectionUtils.isNotEmpty(existed)) {
            clientHttpRequestInterceptors.addAll(existed);
        }
        restTemplate.setInterceptors(clientHttpRequestInterceptors);
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    @Bean
    @ConditionalOnMissingBean
    public RestTemplateAuthorizationInterceptor restTemplateAuthorizationInterceptor(JwtSecurityProperties jwtSecurityProperties) {
        return new RestTemplateAuthorizationInterceptor(jwtSecurityProperties);
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public DefaultConverter defaultConverter(ModelMapper modelMapper) {
        return new DefaultConverterImp(modelMapper);
    }

}
