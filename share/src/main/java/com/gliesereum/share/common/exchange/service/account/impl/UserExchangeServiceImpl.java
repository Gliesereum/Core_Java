package com.gliesereum.share.common.exchange.service.account.impl;

import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import org.apache.commons.collections4.CollectionUtils;
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
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class UserExchangeServiceImpl implements UserExchangeService {

    private RestTemplate restTemplate;

    private ExchangeProperties exchangeProperties;

    @Autowired
    public UserExchangeServiceImpl(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
        this.restTemplate = restTemplate;
        this.exchangeProperties = exchangeProperties;
    }

    @Override
    public boolean userIsExist(UUID userId) {
        boolean result = false;
        if (userId != null) {
            String uri = UriComponentsBuilder
                    .fromUriString(exchangeProperties.getAccount().getUserIsExist())
                    .queryParam("id", userId)
                    .build()
                    .toUriString();
            Map response = restTemplate.getForObject(uri, Map.class);
            if ((response != null) && (response.containsKey("result"))) {
                result = (Boolean) response.get("result");
            }
        }
        return result;
    }

//    @Override
//    public boolean userKYCPassed(UUID userId) {
//        boolean result = false;
//        if (userId != null) {
//            String uri = UriComponentsBuilder
//                    .fromUriString(exchangeProperties.getAccount().getUserKYCPassed())
//                    .queryParam("id", userId)
//                    .build()
//                    .toUriString();
//            Map response = restTemplate.getForObject(uri, Map.class);
//            if ((response != null) && (response.containsKey("result"))) {
//                result = (Boolean) response.get("result");
//            }
//        }
//        return result;
//    }

    @Override
    public List<UserDto> findByIds(Collection<UUID> ids) {
        List<UserDto> result = null;
        if(CollectionUtils.isNotEmpty(ids)) {
            String uri = UriComponentsBuilder
                    .fromUriString(exchangeProperties.getAccount().getFindByIds())
                    .queryParam("ids", ids.toArray())
                    .build()
                    .toString();
            ResponseEntity<List<UserDto>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<List<UserDto>>() {});
            if ((response.getStatusCode().is2xxSuccessful()) && (response.hasBody())) {
                result = response.getBody();
            }
        }
        return result;
    }
}
