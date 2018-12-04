package com.gliesereum.account.appender;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gliesereum.account.mq.MessagePublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @since 11/9/18
 */
@Slf4j
@Service
public class PublisherRedisServiceImpl implements PublisherRedisService {

    @Autowired
    private MessagePublisher publisher;

    @Autowired
    private Environment environment;

    @Autowired
    private ObjectMapper objectMapper;

    private final String CHANEL_LOGGING = "spring.redis.chanel-logstash";

    private final String SERVICE_NAME = "spring.application.name";

    @Async
    @Override
    public void publishing(String message) {
        publisher.publish(message, environment.getProperty(CHANEL_LOGGING));
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
