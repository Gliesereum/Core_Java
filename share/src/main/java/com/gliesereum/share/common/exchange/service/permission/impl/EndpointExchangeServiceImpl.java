package com.gliesereum.share.common.exchange.service.permission.impl;

import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.permission.EndpointExchangeService;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointPackDto;
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
public class EndpointExchangeServiceImpl implements EndpointExchangeService {

    private RestTemplate restTemplate;

    private ExchangeProperties exchangeProperties;

    @Autowired
    public EndpointExchangeServiceImpl(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
        this.restTemplate = restTemplate;
        this.exchangeProperties = exchangeProperties;
    }

    @Override
    public List<EndpointDto> createPack(EndpointPackDto endpointPack) {
        List<EndpointDto> result = null;
        if (endpointPack != null) {
            ResponseEntity<List<EndpointDto>> response = restTemplate.exchange(
                    exchangeProperties.getPermission().getEndpointUpdatePack(),
                    HttpMethod.POST,
                    new HttpEntity<>(endpointPack),
                    new ParameterizedTypeReference<List<EndpointDto>>() {
                    });
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                result = response.getBody();
            }
        }

        return result;
    }
}
