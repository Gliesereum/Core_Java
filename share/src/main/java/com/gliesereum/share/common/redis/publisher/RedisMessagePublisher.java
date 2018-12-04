package com.gliesereum.share.common.redis.publisher;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 04/12/2018
 */
public interface RedisMessagePublisher {

    void publish(final String message, final String topic);
}
