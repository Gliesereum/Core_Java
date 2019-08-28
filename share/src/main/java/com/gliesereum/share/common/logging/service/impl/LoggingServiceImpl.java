package com.gliesereum.share.common.logging.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gliesereum.share.common.logging.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${spring.application.name}")
    private String serviceName;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queueLogstashSystem;

    @Autowired
    private Queue queueLogstashRequest;

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
                ((ObjectNode) jsonNode).put("service_name", serviceName);
                publishing(jsonNode, queueLogstashRequest.getName());
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
                ((ObjectNode) jsonNode).put("service_name", serviceName);
                publishing(jsonNode, queueLogstashSystem.getName());
            } catch (Exception e) {
                log.warn("Error while logging: {} ", e.getMessage());
            }
        }
    }
}
