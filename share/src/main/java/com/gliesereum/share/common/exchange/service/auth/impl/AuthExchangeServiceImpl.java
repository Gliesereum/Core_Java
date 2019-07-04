package com.gliesereum.share.common.exchange.service.auth.impl;

import com.gliesereum.share.common.exchange.service.auth.AuthExchangeService;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.SERVICE_NOT_AVAILABLE;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class AuthExchangeServiceImpl implements AuthExchangeService {

    private RestTemplate restTemplate;

    private SecurityProperties securityProperties;

    @Autowired
    public AuthExchangeServiceImpl(RestTemplate restTemplate, SecurityProperties securityProperties) {
        this.restTemplate = restTemplate;
        this.securityProperties = securityProperties;
    }

    @Override
    public AuthDto checkAccessToken(String accessToken) {
        AuthDto result = null;
        try {
            if (StringUtils.isNotBlank(accessToken)) {
                String uri = UriComponentsBuilder
                        .fromUriString(securityProperties.getCheckAccessUrl())
                        .queryParam("accessToken", accessToken)
                        .build()
                        .toUriString();
                result = restTemplate.getForObject(uri, AuthDto.class);
            }
        } catch (IllegalStateException e) {
            throw new CustomException(SERVICE_NOT_AVAILABLE, e);
        }
        return result;
    }
}
