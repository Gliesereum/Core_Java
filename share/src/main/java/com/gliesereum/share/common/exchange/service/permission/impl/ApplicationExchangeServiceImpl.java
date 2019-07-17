package com.gliesereum.share.common.exchange.service.permission.impl;

import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exchange.service.permission.ApplicationExchangeService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.SERVICE_NOT_AVAILABLE;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class ApplicationExchangeServiceImpl implements ApplicationExchangeService {

    private RestTemplate restTemplate;

    private SecurityProperties securityProperties;

    @Autowired
    public ApplicationExchangeServiceImpl(RestTemplate restTemplate, SecurityProperties securityProperties) {
        this.restTemplate = restTemplate;
        this.securityProperties = securityProperties;
    }

    @Override
    public ApplicationDto check(UUID applicationId) {
        ApplicationDto result = null;
        try {
            if (applicationId != null) {
                String uri = UriComponentsBuilder
                        .fromUriString(securityProperties.getCheckApplicationId())
                        .queryParam("id", applicationId)
                        .build()
                        .toUriString();
                result = restTemplate.getForObject(uri, ApplicationDto.class);
            }
        } catch (IllegalStateException e) {
            throw new CustomException(SERVICE_NOT_AVAILABLE, e);
        }
        return result;
    }
}
