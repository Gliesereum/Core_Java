package com.gliesereum.share.common.exchange.service.mail.impl;

import com.gliesereum.share.common.exchange.properties.ExchangeProperties;
import com.gliesereum.share.common.exchange.service.mail.MailExchangeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class MailExchangeServiceImpl implements MailExchangeService {

    private RestTemplate restTemplate;

    private ExchangeProperties exchangeProperties;

    @Autowired
    public MailExchangeServiceImpl(RestTemplate restTemplate, ExchangeProperties exchangeProperties) {
        this.restTemplate = restTemplate;
        this.exchangeProperties = exchangeProperties;
    }

    @Override
    public String sendEmailVerification(String to, String code) {
        return sendVerification(to, code, exchangeProperties.getMail().getEmailVerification());
    }

    @Override
    public String sendPhoneVerification(String to, String code) {
        return sendVerification(to, code, exchangeProperties.getMail().getPhoneVerification());
    }

    private String sendVerification(String to, String code, String url) {
        String result = null;
        if (!StringUtils.isAllEmpty(to, code)) {
            String uri = UriComponentsBuilder
                    .fromUriString(url)
                    .queryParam("to", to)
                    .queryParam("message", code)
                    .build()
                    .toUriString();
            Map response = restTemplate.postForObject(uri, new HashMap<>(), Map.class);
            if ((response != null) && (response.containsKey("result"))) {
                result = (String) response.get("result");
            }
        }
        return result;
    }
}
