package com.gliesereum.proxy.service.exchange.permission.impl;

import com.gliesereum.proxy.config.security.properties.SecurityProperties;
import com.gliesereum.proxy.service.exchange.permission.GroupService;
import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.model.dto.permission.permission.PermissionMapValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.SERVICE_NOT_AVAILABLE;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Cacheable(value = "group", key = "#groupId", unless = "#result == null")
    public Map<String, PermissionMapValue> getPermissionMap(UUID groupId) {
        Map<String, PermissionMapValue> result = null;
        try {
            if (groupId != null) {
                String uri = UriComponentsBuilder
                        .fromUriString(securityProperties.getGetPermissionMapUrl())
                        .queryParam("groupId", groupId)
                        .build()
                        .toUriString();
                ResponseEntity<Map<String, PermissionMapValue>> response = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        HttpEntity.EMPTY,
                        new ParameterizedTypeReference<Map<String, PermissionMapValue>>() {
                        }
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
