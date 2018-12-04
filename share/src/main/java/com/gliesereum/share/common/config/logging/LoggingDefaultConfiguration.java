package com.gliesereum.share.common.config.logging;

import com.gliesereum.share.common.config.redis.RedisDefaultConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 04/12/2018
 */

@Configuration
@ComponentScan(
        value = "com.gliesereum.share.common.logging",
        basePackageClasses = RedisDefaultConfiguration.class)
public class LoggingDefaultConfiguration {
}
