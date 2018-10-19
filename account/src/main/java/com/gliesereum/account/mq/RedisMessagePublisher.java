package com.gliesereum.account.mq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

/**
 * @author vitalij
 * @since 10/18/18
 */
@Component
public class RedisMessagePublisher implements MessagePublisher {

    @Autowired
    private ChannelTopic topic;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(final String message) {
        redisTemplate.convertAndSend(topic.getTopic(), message);
    }
}
