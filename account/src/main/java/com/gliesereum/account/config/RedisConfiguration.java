package com.gliesereum.account.config;

import com.gliesereum.share.common.config.redis.RedisDefaultConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackageClasses = RedisDefaultConfiguration.class)
@EnableRedisRepositories("com.gliesereum.account.model.repository.redis")
public class RedisConfiguration {
}
