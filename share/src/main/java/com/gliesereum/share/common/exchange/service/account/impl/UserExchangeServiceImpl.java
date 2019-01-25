package com.gliesereum.share.common.exchange.service.account.impl;

import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

    @Override
    public boolean userKYCPassed(UUID userId) {
        boolean result = false;
        if (userId != null) {
            String uri = UriComponentsBuilder
                    .fromUriString(exchangeProperties.getAccount().getUserKYCPassed())
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
}
