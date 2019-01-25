package com.gliesereum.mail.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.mail.email.EmailService;
import com.gliesereum.mail.phone.PhoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@EnableRabbit
@Component
public class RabbitMqListener {

    private final String SUBJECT = "spring.mail.subject";

    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PhoneService phoneService;

    @RabbitListener(queues = "verificationQueue")
    public void processQueue(String message) {
        Map<String, String> model = getModelByJson(message);
        if (!CollectionUtils.isEmpty(model)) {
            if (!StringUtils.isEmpty(model.get("type"))) {
                switch (model.get("type").toUpperCase()) {
                    case "EMAIL": {
                        emailService.sendSingleVerificationMessage(model.get("value"), environment.getProperty(SUBJECT), model.get("code"));
                        break;
                    }
                    case "PHONE": {
                        phoneService.sendSingleMessage(model.get("value"), model.get("code"));
                        break;
                    }
                }
            }
        }
    }

    private Map<String, String> getModelByJson(String json) {
        Map<String, String> result = new HashMap<String, String>();
        try {
            result = (HashMap<String, String>) new ObjectMapper().readValue(json, HashMap.class);
        } catch (IOException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
