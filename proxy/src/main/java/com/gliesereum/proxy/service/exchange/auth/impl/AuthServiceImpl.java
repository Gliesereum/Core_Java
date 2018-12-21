package com.gliesereum.proxy.service.exchange.auth.impl;

import com.gliesereum.proxy.config.security.properties.SecurityProperties;
import com.gliesereum.proxy.service.exchange.auth.AuthService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 16/10/2018
 */

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    @Cacheable(value = "tokenInfo", key="#accessToken", unless="#result == null")
    public AuthDto checkAccessToken(String accessToken) {
        AuthDto result = null;
        if (StringUtils.isNotBlank(accessToken)) {
            String uri = UriComponentsBuilder
                    .fromUriString(securityProperties.getCheckAccessUrl())
                    .queryParam("accessToken", accessToken)
                    .build()
                    .toUriString();
            result = restTemplate.getForObject(uri, AuthDto.class);
        }
        return result;
    }
}
