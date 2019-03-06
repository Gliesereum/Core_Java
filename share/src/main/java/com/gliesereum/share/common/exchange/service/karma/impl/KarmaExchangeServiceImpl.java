package com.gliesereum.share.common.exchange.service.karma.impl;

import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.karma.KarmaExchangeService;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class KarmaExchangeServiceImpl implements KarmaExchangeService {

    private RestTemplate restTemplate;

    private ExchangeProperties exchangeProperties;

    @Autowired
    public KarmaExchangeServiceImpl(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
        this.restTemplate = restTemplate;
        this.exchangeProperties = exchangeProperties;
    }

    @Override
    public List<BaseBusinessDto> getBusinessForCurrentUser() {
        List<BaseBusinessDto> result = null;
        ResponseEntity<List<BaseBusinessDto>> response = restTemplate.exchange(
                exchangeProperties.getKarma().getBusinessForCurrentUser(),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<BaseBusinessDto>>() {
                }
        );
        if ((response.getStatusCode().is2xxSuccessful())) {
            result = response.getBody();
        }

        return result;
    }
}
