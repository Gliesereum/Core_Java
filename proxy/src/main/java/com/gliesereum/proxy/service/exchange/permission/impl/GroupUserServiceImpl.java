package com.gliesereum.proxy.service.exchange.permission.impl;

import com.gliesereum.share.common.security.properties.SecurityProperties;
import com.gliesereum.proxy.service.exchange.permission.GroupUserService;
import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.SERVICE_NOT_AVAILABLE;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class GroupUserServiceImpl implements GroupUserService {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Cacheable(value = "userGroup", key = "#id", unless = "#result == null")
    public List<GroupDto> getUserGroups(UUID id, String jwt) {
        List<GroupDto> result = null;
        try {
            if (StringUtils.isNotEmpty(jwt)) {
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(securityProperties.getJwtHeader(), securityProperties.getJwtPrefix() + " " + jwt);

                ResponseEntity<List<GroupDto>> response = restTemplate.exchange(
                        securityProperties.getGetUserGroupUrl(),
                        HttpMethod.GET,
                        new HttpEntity<>(httpHeaders),
                        new ParameterizedTypeReference<List<GroupDto>>() {}
                );
                if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                    result = response.getBody();
                }
            }
        } catch (IllegalStateException e) {
            throw new CustomException(SERVICE_NOT_AVAILABLE, e);
        }
        return result;
    }
}
