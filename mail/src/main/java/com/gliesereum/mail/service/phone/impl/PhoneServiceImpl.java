package com.gliesereum.mail.service.phone.impl;

import com.gliesereum.mail.service.email.EmailService;
import com.gliesereum.mail.service.mail.MailStateService;
import com.gliesereum.mail.service.phone.PhoneService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.mail.MailStateDto;
import com.gliesereum.share.common.model.dto.mail.PhoneResponseDto;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.NOT_SEND;

/**
 * @author vitalij
 */
@Service
public class PhoneServiceImpl implements PhoneService {

    private final static Logger logger = LoggerFactory.getLogger(PhoneServiceImpl.class);

    private final String API_PHONE_URL = "phone.url";
    private final String TOKEN = "phone.token";
    private final String ALPHA_NAME = "phone.alpha-name";
    private final String LOG_EMAIL = "spring.mail.log-email";

    @Autowired
    private Environment environment;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailStateService stateService;

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, environment.getProperty(TOKEN));
        return httpHeaders;
    }

    @Override
    public void sendSingleMessage(String phone, String text) {
        try {
            final String url = environment.getProperty(API_PHONE_URL) + "send";

            Map<String, Object> body = new HashMap<>();
            body.put("originator", environment.getProperty(ALPHA_NAME));
            body.put("lifetime", 180);
            body.put("text", text);
            body.put("phones", Arrays.asList(phone));

            ResponseEntity<PhoneResponseDto> responseEntity = sendRequest(body, url, PhoneResponseDto.class);
            PhoneResponseDto response = responseEntity.getBody();

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                String massage = "Message: " + text + "\nSend to phone: " + phone;
                logger.info(massage);
                String logInfoToEmail = massage + "\nBalance: " + checkBalance();
                emailService.sendSimpleMessage(environment.getProperty(LOG_EMAIL), "Dispatch service info", logInfoToEmail);
                Map<String, Object> info = (Map<String, Object>) response.getSuccess_request().get("info");
                stateService.create(new MailStateDto(phone, text, info.get(phone).toString(), responseEntity.getStatusCode().toString(), 0, LocalDateTime.now()));
            } else {
                Map<String, Object> info = (Map<String, Object>) response.getSuccess_request().get("additional_info");
                emailService.sendSimpleMessage(environment.getProperty(LOG_EMAIL), "Phone service error", info.toString());
                logger.error("Error send message: {} to phone: {} date: {}, return http status: {}", text, phone, new Date().toString(), responseEntity.getStatusCodeValue());
                throw new ClientException(NOT_SEND);
            }
        } catch (Exception e) {
            String error = "Error to send request for send phone message: " + e.getMessage();
            logger.error(error);
            throw e;
        }
    }

    @Override
    public String checkBalance() {
        try {
            final String url = environment.getProperty(API_PHONE_URL) + "balance";
            ResponseEntity<PhoneResponseDto> responseEntity = sendRequest(null, url, PhoneResponseDto.class);
            PhoneResponseDto response = responseEntity.getBody();
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> responseObject = response.getSuccessful_request();
                String balance = responseObject.get("money").toString().concat(" ")
                        .concat(responseObject.get("currency").toString());
                logger.info("Balance: {} date: {}", balance, new Date().toString());
                return balance;
            } else {
                String error = "Error check balance";
                logger.error(error);
                return error;
            }
        } catch (Exception e) {
            String error = "Error to send request for check balance: " + e.getMessage();
            logger.error(error);
            throw e;
        }
    }

    @Scheduled(fixedDelay = 60000)
    private void checkStatus() {
        LocalDateTime date = LocalDateTime.now().minusMinutes(3L);
        List<MailStateDto> list = stateService.getByMessageStatusAndDateAfter(0, date);
        checkStatus(list);
    }

    private void checkStatus(List<MailStateDto> list) {
        if (CollectionUtils.isNotEmpty(list)) {

            final String url = environment.getProperty(API_PHONE_URL) + "status";

            Map<String, Object> body = new HashMap<>();
            body.put("id_sms", list.stream().map(MailStateDto::getMessageId).toArray());

            ResponseEntity<PhoneResponseDto> responseEntity = sendRequest(body, url, PhoneResponseDto.class);
            PhoneResponseDto response = responseEntity.getBody();

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> statuses = response.getSuccessful_request();
                List<MailStateDto> result = new ArrayList<>();
                list.forEach(f -> {
                    Integer status = Integer.valueOf(statuses.get(f.getMessageId()).toString());
                    if (status != f.getMessageStatus()) {
                        f.setMessageStatus(status);
                        result.add(f);
                    }
                });
                if (CollectionUtils.isNotEmpty(result)) {
                    stateService.update(result);
                }
            }
        }
    }


    private <T> ResponseEntity<T> sendRequest(Map<String, Object> body, String url, Class<T> responseClass) {
        HttpEntity httpEntity = null;
        if (body != null) {
            httpEntity = new HttpEntity<>(body, getHeaders());
        } else httpEntity = new HttpEntity<>(getHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseClass);
    }
}
