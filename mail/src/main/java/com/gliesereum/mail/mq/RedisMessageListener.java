package com.gliesereum.mail.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gliesereum.mail.email.EmailService;
import com.gliesereum.mail.phone.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vitalij
 * @since 10/17/18
 */

@Component
public class RedisMessageListener implements MessageListener {

    private final static Logger logger = LoggerFactory.getLogger(RedisMessageListener.class);

    private final String SUBJECT = "spring.mail.subject";

    @Autowired
    private Environment environment;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PhoneService phoneService;


    @Override
    public void onMessage(final Message message, final byte[] pattern) {
        Map<String, String> model = getModelByJson(message.toString());
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
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}