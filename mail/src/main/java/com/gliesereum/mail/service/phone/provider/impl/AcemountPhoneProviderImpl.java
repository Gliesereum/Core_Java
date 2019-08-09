package com.gliesereum.mail.service.phone.provider.impl;

import com.gliesereum.mail.service.phone.provider.PhoneProvider;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.mail.MailStateDto;
import com.gliesereum.share.common.model.dto.mail.AcemountPhoneResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.SERVER_ERROR;
import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.NOT_SEND;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service("acemountPhoneProvider")
public class AcemountPhoneProviderImpl implements PhoneProvider {

    private static final String SEND_URL = "send";
    private static final String BALANCE_URL = "balance";
    private static final String STATUS_URL = "status";

    @Value("${phone.acemount.url}")
    private String apiUrl;

    @Value("${phone.acemount.token}")
    private String apiToken;

    @Value("${phone.acemount.alpha-name}")
    private String apiAlphaName;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public MailStateDto sendSingleMessage(String phone, String text) {
        MailStateDto result = null;
        final String url = apiUrl + SEND_URL;
        Map<String, Object> body = Map.of(
                "originator", apiAlphaName,
                "lifetime", 180,
                "text", text,
                "phones", Arrays.asList(phone));

        ResponseEntity<AcemountPhoneResponseDto> responseEntity;
        try {
            responseEntity = sendRequest(body, url, AcemountPhoneResponseDto.class);
        } catch (Exception e) {
            log.error("Error to send request for send phone message", e);
            throw new ClientException(SERVER_ERROR);
        }
        AcemountPhoneResponseDto response = responseEntity.getBody();
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            Map<String, Object> info = (Map<String, Object>) response.getSuccess_request().get("info");
            result = new MailStateDto(phone, text, info.get(phone).toString(), responseEntity.getStatusCode().toString(), "0", LocalDateTime.now());
        } else {
            log.error("Error send message: {} to phone: {} date: {}, return http status: {}", text, phone, new Date(), responseEntity.getStatusCodeValue());
            throw new ClientException(NOT_SEND);
        }
        return result;
    }

    @Override
    public String checkBalance() {
        try {
            final String url = apiUrl + BALANCE_URL;
            ResponseEntity<AcemountPhoneResponseDto> responseEntity = sendRequest(null, url, AcemountPhoneResponseDto.class);
            AcemountPhoneResponseDto response = responseEntity.getBody();
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseObject = response.getSuccessful_request();
                String balance = responseObject.get("money").toString().concat(" ")
                        .concat(responseObject.get("currency").toString());
                log.info("Balance: {} date: {}", balance, new Date());
                return balance;
            } else {
                String error = "Error check balance";
                log.error(error);
                return error;
            }
        } catch (Exception e) {
            String error = "Error to send request for check balance: " + e.getMessage();
            log.error(error);
            throw e;
        }
    }

    @Override
    public List<MailStateDto> checkStatus(List<MailStateDto> list) {
        List<MailStateDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            final String url = apiUrl + STATUS_URL;

            Map<String, Object> body = new HashMap<>();
            body.put("id_sms", list.stream().map(MailStateDto::getMessageId).toArray());

            ResponseEntity<AcemountPhoneResponseDto> responseEntity = sendRequest(body, url, AcemountPhoneResponseDto.class);
            AcemountPhoneResponseDto response = responseEntity.getBody();

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> statuses = response.getSuccessful_request();
                list.forEach(f -> {
                    String status = statuses.get(f.getMessageId()).toString();
                    if (status != f.getMessageStatus()) {
                        f.setMessageStatus(status);
                        result.add(f);
                    }
                });
            }
        }
        return result;
    }

    private <T> ResponseEntity<T> sendRequest(Map<String, Object> body, String url, Class<T> responseClass) {
        HttpEntity httpEntity;
        if (body != null) {
            httpEntity = new HttpEntity<>(body, getHeaders());
        } else {
            httpEntity = new HttpEntity<>(getHeaders());
        }
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseClass);
    }

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, apiToken);
        return httpHeaders;
    }
}
