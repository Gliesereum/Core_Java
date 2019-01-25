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

    private final String SERVICE_NAME = "spring.application.name";
    private final String QUEUE_LOGSTASH = "spring.rabbitmq.queue-logstash";

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Async
    @Override
    public void publishing(JsonNode jsonNode) {
        try {
            rabbitTemplate.convertAndSend(environment.getRequiredProperty(QUEUE_LOGSTASH), jsonNode);
        } catch (Exception e) {

        }
    }

    @Async
    @Override
    public void publishingObject(Object obj) {
        if (obj != null) {
            try {
                JsonNode jsonNode = objectMapper.valueToTree(obj);
                ((ObjectNode) jsonNode).put("service_name", environment.getRequiredProperty(SERVICE_NAME));
                publishing(jsonNode);
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
