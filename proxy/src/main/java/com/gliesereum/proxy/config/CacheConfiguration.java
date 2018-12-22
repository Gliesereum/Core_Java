package com.gliesereum.proxy.config;

import org.cache2k.extra.spring.SpringCache2kCacheManager;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-21
 */

@EnableCaching
@Configuration
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager() {
        return new SpringCache2kCacheManager()
                .defaultSetup(b -> b.permitNullValues(false))
                .addCaches(
                        b -> b.name("group").expireAfterWrite(1, TimeUnit.MINUTES).entryCapacity(1000),
                        b -> b.name("user-group").expireAfterWrite(1, TimeUnit.MINUTES).entryCapacity(10000),
                        b -> b.name("tokenInfo").expireAfterWrite(1, TimeUnit.MINUTES).entryCapacity(10000)
                );
    }
}
