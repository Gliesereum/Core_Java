package com.gliesereum.share.common.exchange.service.account.impl;

import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.account.UserPhoneExchangeService;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Service
public class UserPhoneExchangeServiceImpl implements UserPhoneExchangeService {

    private RestTemplate restTemplate;

    private ExchangeProperties exchangeProperties;

    @Autowired
    public UserPhoneExchangeServiceImpl(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
        this.restTemplate = restTemplate;
        this.exchangeProperties = exchangeProperties;
    }


    @Override
    public List<UserPhoneDto> findUserPhoneByUserIds(Collection<UUID> ids) {
        List<UserPhoneDto> result = null;
        if(CollectionUtils.isNotEmpty(ids)) {
            String uri = UriComponentsBuilder
                    .fromUriString(exchangeProperties.getAccount().getFindUserPhonesByUserIds())
                    .queryParam("ids", ids.toArray())
                    .build()
                    .toString();
            ResponseEntity<List<UserPhoneDto>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<List<UserPhoneDto>>() {});
            if ((response.getStatusCode().is2xxSuccessful()) && (response.hasBody())) {
                result = response.getBody();
            }
        }
        return result;
    }
    
    @Override
    public void sendCode(String phone) {
        if(StringUtils.isNotBlank(phone)) {
            String uri = UriComponentsBuilder
                    .fromUriString(exchangeProperties.getAccount().getSendCode())
                    .queryParam("phone", phone)
                    .build()
                    .toString();
            ResponseEntity<Map> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<Map>() {});
        }
       
    }
}
