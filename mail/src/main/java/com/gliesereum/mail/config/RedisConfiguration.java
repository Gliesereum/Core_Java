package com.gliesereum.mail.config;

import com.gliesereum.mail.mq.RedisMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author vitalij
 * @since 10/19/18
 */
@Configuration
public class RedisConfiguration {

    private final String CHANEL = "spring.mail.chanel-name";

    @Autowired
    private Environment environment;

    @Bean
    public RedisConnectionFactory connectionFactory(){
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(RedisMessageListener redisMessageListener,
                                                        RedisConnectionFactory redisConnectionFactory,
                                                        ChannelTopic channelTopic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);
        container.addMessageListener(redisMessageListener, channelTopic);
        return container;
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic(environment.getRequiredProperty(CHANEL));
    }


}
