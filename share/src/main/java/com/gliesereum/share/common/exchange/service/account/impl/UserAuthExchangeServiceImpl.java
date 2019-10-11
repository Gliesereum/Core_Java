package com.gliesereum.share.common.exchange.service.account.impl;

import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.account.UserAuthExchangeService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.account.auth.SignInDto;
import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserAuthExchangeServiceImpl implements UserAuthExchangeService {
	
	private RestTemplate restTemplate;
	
	private ExchangeProperties exchangeProperties;
	
	@Autowired
	public UserAuthExchangeServiceImpl(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
		this.restTemplate = restTemplate;
		this.exchangeProperties = exchangeProperties;
	}
	
	@Override
	public AuthDto auth(String phone, String code) {
		AuthDto result = null;
		if (StringUtils.isNotBlank(phone) && StringUtils.isNotBlank(code)) {
			SignInDto signInDto = new SignInDto();
			signInDto.setCode(code);
			signInDto.setValue(phone);
			signInDto.setType(VerificationType.PHONE);
			
			
			ResponseEntity<AuthDto> response = restTemplate.exchange(
					exchangeProperties.getAccount().getUserAuth(),
					HttpMethod.POST,
					new HttpEntity<>(signInDto),
					new ParameterizedTypeReference<AuthDto>() {
					});
			if ((response.getStatusCode().is2xxSuccessful()) && (response.hasBody())) {
				result = response.getBody();
			}
		}
		return result;
	}
}
