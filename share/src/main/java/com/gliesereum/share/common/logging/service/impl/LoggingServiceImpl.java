package com.gliesereum.share.common.logging.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gliesereum.share.common.logging.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class LoggingServiceImpl implements LoggingService {

    private static final String SERVICE_NAME = "spring.application.name";
    private static final String QUEUE_LOGSTASH_SYSTEM = "spring.rabbitmq.queue-logstash-system";
    private static final String QUEUE_LOGSTASH_REQUEST = "spring.rabbitmq.queue-logstash-request";

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Async
    @Override
    public void publishing(JsonNode jsonNode, String queueName) {
        try {
            rabbitTemplate.convertAndSend(queueName, jsonNode);
        } catch (Exception e) {
            log.warn("Error while logging: {} ", e.getMessage());
        }
    }

    @Async
    @Override
    public void publishingRequestObject(Object obj) {
        if (obj != null) {
            try {
                JsonNode jsonNode = objectMapper.valueToTree(obj);
                ((ObjectNode) jsonNode).put("service_name", environment.getRequiredProperty(SERVICE_NAME));
                publishing(jsonNode, environment.getRequiredProperty(QUEUE_LOGSTASH_REQUEST));
            } catch (Exception e) {
                log.warn("Error while logging: {} ", e.getMessage());
            }
        }
    }

    @Async
    @Override
    public void publishingSystemObject(Object obj) {
        if (obj != null) {
            try {
                JsonNode jsonNode = objectMapper.valueToTree(obj);
                ((ObjectNode) jsonNode).put("service_name", environment.getRequiredProperty(SERVICE_NAME));
                publishing(jsonNode, environment.getRequiredProperty(QUEUE_LOGSTASH_SYSTEM));
            } catch (Exception e) {
                log.warn("Error while logging: {} ", e.getMessage());
            }
        }
    }
}
