package com.gliesereum.share.common.logging.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gliesereum.share.common.logging.service.LoggingRedisService;
import com.gliesereum.share.common.redis.publisher.RedisMessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 04/12/2018
 */

@Slf4j
@Service
public class LoggingRedisServiceImpl implements LoggingRedisService {

    @Autowired
    private RedisMessagePublisher redisMessagePublisher;

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper objectMapper;

    private final String CHANEL_LOGGING = "spring.redis.chanel-logstash";

    private final String SERVICE_NAME = "spring.application.name";

    @Async
    @Override
    public void publishing(String message) {
        redisMessagePublisher.publish(message, environment.getProperty(CHANEL_LOGGING));
    }

    @Async
    @Override
    public void publishingObject(Object obj) {
        if (obj != null) {
            try {
                JsonNode jsonNode = objectMapper.valueToTree(obj);
                ((ObjectNode) jsonNode).put("service_name", environment.getRequiredProperty(SERVICE_NAME));
                final String json = jsonNode.toString();
                publishing(json);
            } catch (Exception e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
