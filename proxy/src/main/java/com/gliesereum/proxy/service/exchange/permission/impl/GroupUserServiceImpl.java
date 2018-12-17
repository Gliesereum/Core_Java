package com.gliesereum.proxy.service.exchange.permission.impl;

import com.gliesereum.proxy.config.security.properties.SecurityProperties;
import com.gliesereum.proxy.service.exchange.permission.GroupUserService;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-07
 */

@Service
public class GroupUserServiceImpl implements GroupUserService {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public GroupDto getUserGroup(String jwt) {
        GroupDto result = null;
        if (StringUtils.isNotEmpty(jwt)) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(securityProperties.getJwtHeader(), securityProperties.getJwtPrefix() + " " + jwt);

            ResponseEntity<GroupDto> response = restTemplate.exchange(
                    securityProperties.getGetUserGroupUrl(),
                    HttpMethod.GET,
                    new HttpEntity<>(httpHeaders),
                    GroupDto.class
            );
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                result = response.getBody();
            }
        }
        return result;
    }
}