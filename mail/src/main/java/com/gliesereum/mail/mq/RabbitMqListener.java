package com.gliesereum.mail.mq;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.mail.email.EmailService;
import com.gliesereum.mail.phone.PhoneService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.Queue;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private PhoneService phoneService;

    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queuesToDeclare = @Queue(name = "verificationQueue", ignoreDeclarationExceptions = "true"))
    public void processQueue(String message) {
        Map<String, String> model = getModelByJson(message);
        if (!CollectionUtils.isEmpty(model)) {
            if (!StringUtils.isEmpty(model.get("type"))) {
                switch (model.get("type").toUpperCase()) {
                    case "EMAIL": {
                        emailService.sendSingleVerificationMessage(model.get("value"), model.get("code"));
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
        Map<String, String> result = new HashMap<>();
        try {
            result = objectMapper.readValue(json, new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            log.error("Error while read json", e);
        }
        return result;
    }
}
