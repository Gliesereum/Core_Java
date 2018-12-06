package com.gliesereum.share.common.config.redis;

import com.gliesereum.share.common.redis.publisher.RedisMessagePublisher;
import com.gliesereum.share.common.redis.publisher.impl.RedisMessagePublisherImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 04/12/2018
 */
@Configuration
public class RedisDefaultConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        template.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        return template;
    }

    @Bean
    @ConditionalOnMissingBean
    public RedisMessagePublisher redisMessagePublisher(RedisTemplate<String, Object> redisTemplate) {
        return new RedisMessagePublisherImpl(redisTemplate);
    }
}
