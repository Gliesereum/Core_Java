package com.gliesereum.mail.service.phone.impl;

import com.gliesereum.mail.service.email.EmailService;
import com.gliesereum.mail.service.mail.MailStateService;
import com.gliesereum.mail.service.phone.PhoneService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.mail.MailStateDto;
import com.gliesereum.share.common.model.dto.mail.PhoneResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.SERVER_ERROR;
import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.NOT_SEND;

/**
 * @author vitalij
 */
@Service
@Slf4j
public class PhoneServiceImpl implements PhoneService {

    @Value("${phone.url}")
    private String apiUrl;

    @Value("${phone.token}")
    private String apiToken;

    @Value("${phone.alpha-name}")
    private String apiAlphaName;

    @Value("${spring.mail.log-email}")
    private String logEmail;

    @Value("${sendSms}")
    private Boolean sendSmsEnable;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MailStateService stateService;

    @Autowired
    private TaskExecutor taskExecutor;

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add(HttpHeaders.AUTHORIZATION, apiToken);
        return httpHeaders;
    }

    @Override
    public void sendSingleMessage(String phone, String text) {
        final String url = apiUrl + "send";
        Map<String, Object> body = Map.of(
                "originator", apiAlphaName,
                "lifetime", 180,
                "text", text,
                "phones", Arrays.asList(phone));

        String message = "Message: " + text + "\nSend to phone: " + phone;
        log.info(message);

        if (sendSmsEnable) {
            ResponseEntity<PhoneResponseDto> responseEntity;
            try {
                responseEntity = sendRequest(body, url, PhoneResponseDto.class);
            } catch (Exception e) {
                log.error("Error to send request for send phone message", e);
                throw new ClientException(SERVER_ERROR);
            }
            PhoneResponseDto response = responseEntity.getBody();

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Successful: {}", message);
                sendLogInfoToEmailAsync(message);
                Map<String, Object> info = (Map<String, Object>) response.getSuccess_request().get("info");
                stateService.create(new MailStateDto(phone, text, info.get(phone).toString(), responseEntity.getStatusCode().toString(), 0, LocalDateTime.now()));
            } else {
                Map<String, Object> info = (Map<String, Object>) response.getSuccess_request().get("additional_info");
                emailService.sendSimpleMessageAsync(logEmail, "Phone service error", info.toString());
                log.error("Error send message: {} to phone: {} date: {}, return http status: {}", text, phone, new Date(), responseEntity.getStatusCodeValue());
                throw new ClientException(NOT_SEND);

            }
        }
    }

    @Override
    public String checkBalance() {
        try {
            final String url = apiUrl + "balance";
            ResponseEntity<PhoneResponseDto> responseEntity = sendRequest(null, url, PhoneResponseDto.class);
            PhoneResponseDto response = responseEntity.getBody();
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

    @Scheduled(fixedDelay = 60000)
    private void checkStatus() {
        LocalDateTime date = LocalDateTime.now().minusMinutes(3L);
        List<MailStateDto> list = stateService.getByMessageStatusAndDateAfter(0, date);
        checkStatus(list);
    }

    private void checkStatus(List<MailStateDto> list) {
        if (CollectionUtils.isNotEmpty(list)) {

            final String url = apiUrl + "status";

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

    private void sendLogInfoToEmailAsync(String message) {
        taskExecutor.execute(() -> {
            String logInfoToEmail = message + "\nBalance: " + checkBalance();
            emailService.sendSimpleMessage(logEmail, "Dispatch service info", logInfoToEmail);

        });
    }

    private <T> ResponseEntity<T> sendRequest(Map<String, Object> body, String url, Class<T> responseClass) {
        HttpEntity httpEntity = null;
        if (body != null) {
            httpEntity = new HttpEntity<>(body, getHeaders());
        } else httpEntity = new HttpEntity<>(getHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, responseClass);
    }
}
