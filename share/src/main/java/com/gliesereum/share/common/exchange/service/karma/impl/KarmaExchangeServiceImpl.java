package com.gliesereum.share.common.exchange.service.karma.impl;

import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.karma.KarmaExchangeService;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    @Override
    public boolean existChatSupport(UUID businessId, UUID userId) {
        boolean result = false;
        if (ObjectUtils.allNotNull(businessId, userId)) {
            String uri = UriComponentsBuilder
                    .fromUriString(exchangeProperties.getKarma().getExistChatSupport())
                    .queryParam("userId", userId)
                    .queryParam("businessId", businessId)
                    .build()
                    .toUriString();
            Map response = restTemplate.getForObject(uri, Map.class);
            if ((response != null) && (response.containsKey("result"))) {
                Object value = response.get("result");
                if (value instanceof Boolean) {
                    result = (Boolean) value;
                }
            }
        }
        return result;
    }
}
