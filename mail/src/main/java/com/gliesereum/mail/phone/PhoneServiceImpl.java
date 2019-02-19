package com.gliesereum.mail.phone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.mail.email.EmailService;
import com.gliesereum.mail.phone.response.PhoneResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vitalij
 */
@Service
public class PhoneServiceImpl implements PhoneService {

    private final static Logger logger = LoggerFactory.getLogger(PhoneServiceImpl.class);

    private final String API_PHONE_URL = "phone.url";
    private final String AUTH_TYPE = "phone.auth-type";
    private final String LOGIN = "phone.login";
    private final String TOKEN = "phone.token";
    private final String ALPHA_NAME = "phone.alpha-name";
    private final String ADMIN_EMAIL = "admin.email";

    @Autowired
    private Environment environment;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private EmailService emailService;

    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }

    @Override
    public void sendSingleMessage(String phone, String text) {
        try {
            final String url = environment.getProperty(API_PHONE_URL) + "single";

            Map<String, String> body = getBody();
            body.put("alphaname", environment.getProperty(ALPHA_NAME));
            body.put("text", text);
            body.put("phone", phone);

            ResponseEntity<String> responseEntity = sendRequest(body, url);
            PhoneResponse response = mapper.readValue(responseEntity.getBody(), PhoneResponse.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                logger.info("Massage: {} send to phone: {}", text, phone);
            } else {
                logger.error(response.getError());
                emailService.sendSimpleMessage(environment.getProperty(ADMIN_EMAIL), "Phone service error", response.getError());
            }
        } catch (Exception e) {
            String error = "Error to send request for send phone message: " + e.getMessage();
            logger.error(error);
        }
    }

    @Override
    public String checkBalance() {
        try {
            final String url = environment.getProperty(API_PHONE_URL) + "balance";

            ResponseEntity<String> responseEntity = sendRequest(getBody(), url);
            PhoneResponse response = mapper.readValue(responseEntity.getBody(), PhoneResponse.class);

            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                logger.info("Balance: {} date: {}", response.getBalance(), new Date().toString());
                return response.getBalance();
            } else {
                logger.error(response.getError());
                return response.getError();
            }
        } catch (Exception e) {
            String error = "Error to send request for check balance: " + e.getMessage();
            logger.error(error);
            return error;
        }
    }

    private Map<String, String> getBody() {
        Map<String, String> body = new HashMap();
        body.put("auth_type", environment.getProperty(AUTH_TYPE));
        body.put("login", environment.getProperty(LOGIN));
        body.put("token", environment.getProperty(TOKEN));
        return body;
    }

    private ResponseEntity<String> sendRequest(Map<String, String> body, String url) {
        HttpEntity httpEntity = new HttpEntity<>(body, getHeaders());
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }

}
