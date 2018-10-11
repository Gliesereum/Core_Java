package com.gliesereum.account.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */
@Configuration
@EnableRedisRepositories("com.gliesereum.account.model.repository.redis")
public class RedisConfiguration {
}
