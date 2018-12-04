package com.gliesereum.share.common.redis.publisher.impl;

import com.gliesereum.share.common.redis.publisher.RedisMessagePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 04/12/2018
 */

@Service
public class RedisMessagePublisherImpl implements RedisMessagePublisher {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void publish(final String message, final String topic) {
        redisTemplate.convertAndSend(new ChannelTopic(topic).getTopic(), message);
    }
}
