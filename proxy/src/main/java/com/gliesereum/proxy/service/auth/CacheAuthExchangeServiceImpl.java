package com.gliesereum.proxy.service.auth;

import com.gliesereum.share.common.exchange.service.auth.impl.AuthExchangeServiceImpl;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.gliesereum.proxy.config.CacheConfiguration.TOKEN_INFO_CACHE;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class CacheAuthExchangeServiceImpl extends AuthExchangeServiceImpl {

    @Autowired
    public CacheAuthExchangeServiceImpl(RestTemplate restTemplate, SecurityProperties securityProperties) {
        super(restTemplate, securityProperties);
    }

    @Override
    @Cacheable(value = TOKEN_INFO_CACHE, key="#accessToken", unless="#result == null")
    public AuthDto checkAccessToken(String accessToken) {
        return super.checkAccessToken(accessToken);
    }
}
