package com.gliesereum.notification.config;

import com.gliesereum.share.common.exception.handler.RestTemplateErrorHandler;
import com.gliesereum.share.common.exchange.interceptor.RestTemplateAuthorizationInterceptor;
import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.account.UserAuthExchangeService;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.exchange.service.account.UserPhoneExchangeService;
import com.gliesereum.share.common.exchange.service.account.impl.UserAuthExchangeServiceImpl;
import com.gliesereum.share.common.exchange.service.account.impl.UserExchangeServiceImpl;
import com.gliesereum.share.common.exchange.service.account.impl.UserPhoneExchangeServiceImpl;
import com.gliesereum.share.common.exchange.service.karma.KarmaExchangeService;
import com.gliesereum.share.common.exchange.service.karma.impl.KarmaExchangeServiceImpl;
import com.gliesereum.share.common.security.properties.JwtSecurityProperties;
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
 * @author yvlasiuk
 * @version 1.0
 */

@Configuration
@ComponentScan(basePackageClasses = ExchangeProperties.class)
public class ExchangeConfiguration {

	@Bean
	@LoadBalanced
	@ConditionalOnMissingBean
	public RestTemplate restTemplate(
			RestTemplateAuthorizationInterceptor restTemplateAuthorizationInterceptor) {
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
	public RestTemplateAuthorizationInterceptor restTemplateAuthorizationInterceptor(
			JwtSecurityProperties jwtSecurityProperties) {
		return new RestTemplateAuthorizationInterceptor(jwtSecurityProperties);
	}

	@Bean
	public UserExchangeService userExchangeService(RestTemplate restTemplate,
			ExchangeProperties exchangeProperties) {
		return new UserExchangeServiceImpl(restTemplate, exchangeProperties);
	}
	
	@Bean
	public UserPhoneExchangeService userPhoneExchangeService(RestTemplate restTemplate,
	                                                         ExchangeProperties exchangeProperties) {
		return new UserPhoneExchangeServiceImpl(restTemplate, exchangeProperties);
	}
	
	@Bean
	public UserAuthExchangeService userAuthExchangeService(RestTemplate restTemplate, 
	                                                       ExchangeProperties exchangeProperties) {
		return new UserAuthExchangeServiceImpl(restTemplate, exchangeProperties);
	}

	@Bean
	public KarmaExchangeService karmaExchangeService(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
		return new KarmaExchangeServiceImpl(restTemplate, exchangeProperties);
	}

}
