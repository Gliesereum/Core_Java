package com.gliesereum.share.common.exchange.service.permission.impl;

import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.permission.GroupUserExchangeService;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.model.dto.permission.group.GroupUserDto;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class GroupUserExchangeServiceImpl implements GroupUserExchangeService {

    private RestTemplate restTemplate;

    private ExchangeProperties exchangeProperties;

    @Autowired
    public GroupUserExchangeServiceImpl(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
        this.restTemplate = restTemplate;
        this.exchangeProperties = exchangeProperties;
    }

    @Override
    public List<GroupUserDto> addUserByGroupPurpose(UUID userId, GroupPurpose groupPurpose) {
        List<GroupUserDto> result = null;
        if (ObjectUtils.allNotNull(userId, groupPurpose)) {
            String uri = UriComponentsBuilder
                    .fromUriString(exchangeProperties.getPermission().getAddUserByGroupPurpose())
                    .queryParam("userId", userId)
                    .queryParam("groupPurpose", groupPurpose)
                    .build()
                    .toUriString();
            ResponseEntity<List<GroupUserDto>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.POST,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<List<GroupUserDto>>() {});
            if ((response != null) && response.getStatusCode().is2xxSuccessful()) {
                result = response.getBody();
            }
        }
        return result;
    }

    @Override
    public List<UUID> getUserIdsByGroupPurpose(GroupPurpose groupPurpose) {
        List<UUID> result = null;
        if (groupPurpose != null) {
            String uri = UriComponentsBuilder.fromUriString(exchangeProperties.getPermission().getGetGroupUserByGroupPurpose())
                    .queryParam("groupPurpose", groupPurpose)
                    .build()
                    .toUriString();
            ResponseEntity<List<GroupUserDto>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    HttpEntity.EMPTY,
                    new ParameterizedTypeReference<List<GroupUserDto>>() {
                    });
            if ((response != null) && response.getStatusCode().is2xxSuccessful()) {
                List<GroupUserDto> body = response.getBody();
                if (CollectionUtils.isNotEmpty(body)) {
                    result = body.stream().map(GroupUserDto::getUserId).distinct().collect(Collectors.toList());
                }
            }
        }
        return result;
    }
}
