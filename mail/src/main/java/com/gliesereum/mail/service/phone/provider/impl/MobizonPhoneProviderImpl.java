package com.gliesereum.mail.service.phone.provider.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.mail.service.phone.provider.PhoneProvider;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.mail.MailStateDto;
import com.gliesereum.share.common.model.dto.mail.MobizonPhoneResponseDto;
import com.gliesereum.share.common.model.dto.mail.MobizonStatusPhoneResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.SERVER_ERROR;
import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.NOT_SEND;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service(value = "mobizonPhoneProvider")
public class MobizonPhoneProviderImpl implements PhoneProvider {

    private static final String SEND_URL = "message/sendSmsMessage";
    private static final String BALANCE_URL = "user/getownbalance";
    private static final String STATUS_URL = "Message/GetSMSStatus";

    @Value("${phone.mobizon.url}")
    private String apiUrl;

    @Value("${phone.mobizon.token}")
    private String apiToken;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public MailStateDto sendSingleMessage(String phone, String text) {
        MailStateDto result = null;
        final String url = buildRequestUri(SEND_URL);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.put("text", Arrays.asList(text));
        body.put("recipient", Arrays.asList(phone));

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = sendRequest(body, url, String.class);
        } catch (Exception e) {
            log.error("Error to send request for send phone message", e);
            throw new ClientException(SERVER_ERROR);
        }
        String json = responseEntity.getBody();
        try {
            MobizonPhoneResponseDto response = objectMapper.readValue(json, MobizonPhoneResponseDto.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> data = response.getData();
                result = new MailStateDto(phone, text, data.get("messageId").toString(), responseEntity.getStatusCode().toString(), data.get("status").toString(), LocalDateTime.now());
            } else {
                log.error("Error send message: {} to phone: {} date: {}, return http status: {}", text, phone, new Date(), responseEntity.getStatusCodeValue());
                throw new ClientException(NOT_SEND);
            }
        } catch (IOException e) {
            log.warn("Error while read send sms response", e);
            throw new ClientException(SERVER_ERROR);
        }

        return result;
    }

    @Override
    public String checkBalance() {
        try {
            final String url = buildRequestUri(BALANCE_URL);
            ResponseEntity<String> responseEntity = sendRequest(null, url, String.class);
            String json = responseEntity.getBody();
            MobizonPhoneResponseDto response = objectMapper.readValue(json, MobizonPhoneResponseDto.class);
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseObject = response.getData();
                String balance = responseObject.get("balance").toString().concat(" ")
                        .concat(responseObject.get("currency").toString());
                log.info("Balance: {} date: {}", balance, new Date());
                return balance;
            } else {
                String error = "Error check balance";
                log.error(error);
                return error;
            }
        } catch (Exception e) {
            log.error("Error to send request for check balance", e);
            throw new ClientException(SERVER_ERROR);
        }
    }

    @Override
    public List<MailStateDto> checkStatus(List<MailStateDto> list) {
        List<MailStateDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            final String url = buildRequestUri(STATUS_URL);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.put("ids", list.stream().map(MailStateDto::getMessageId).collect(Collectors.toList()));

            ResponseEntity<String> responseEntity = sendRequest(body, url, String.class);
            String json = responseEntity.getBody();
            try {
                MobizonStatusPhoneResponseDto response = objectMapper.readValue(json, MobizonStatusPhoneResponseDto.class);
                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    List<Map<String, Object>> data = response.getData();
                    if (CollectionUtils.isNotEmpty(data)) {
                        Map<String, Object> statusMap = data.stream().collect(Collectors.toMap(i -> String.valueOf(i.get("id")), i -> i.get("status")));
                        list.forEach(i -> {
                            Object status = statusMap.get(i.getMessageId());
                            if (status instanceof String) {
                                i.setMessageStatus((String) status);
                                result.add(i);
                            }
                        });
                    }

                }
            } catch (IOException e) {
                log.warn("Error while read send sms response", e);
                throw new ClientException(SERVER_ERROR);
            }
        }
        return result;
    }

    private String buildRequestUri(String path) {
        return UriComponentsBuilder
                .fromUriString(apiUrl + path)
                .queryParam("output", "json")
                .queryParam("api", "v1")
                .queryParam("apiKey", apiToken)
                .build()
                .toUriString();
    }

    private <T> ResponseEntity<T> sendRequest(MultiValueMap<String, Object> body, String url, Class<T> responseClass) {
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
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        return httpHeaders;
    }
}