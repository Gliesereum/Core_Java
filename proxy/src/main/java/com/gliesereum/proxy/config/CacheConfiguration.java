package com.gliesereum.proxy.config;

import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import org.cache2k.Cache2kBuilder;
import org.cache2k.jcache.ExtendedMutableConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.JCacheCache;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.Caching;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@EnableCaching
@Configuration
public class CacheConfiguration {

    @Bean
    public CacheManager cacheManager(javax.cache.CacheManager jCacheManager) {
        return new JCacheCacheManager(jCacheManager);
    }

    @Bean
    public javax.cache.CacheManager jCacheManager() {
        return Caching.getCachingProvider().getCacheManager();
    }

    @Bean
    public Cache groupCache(javax.cache.CacheManager jCacheManager) {
        javax.cache.Cache groupCache = jCacheManager.createCache("group",
                ExtendedMutableConfiguration.of(new Cache2kBuilder<UUID, Map>(){}
                        .entryCapacity(1000)
                        .expireAfterWrite(1, TimeUnit.MINUTES)));
        return new JCacheCache(groupCache);

    }

    @Bean
    public Cache userGroupCache(javax.cache.CacheManager jCacheManager) {
        javax.cache.Cache groupCache = jCacheManager.createCache("userGroup",
                ExtendedMutableConfiguration.of(new Cache2kBuilder<String, GroupDto>(){}
                        .entryCapacity(10000)
                        .expireAfterWrite(1, TimeUnit.MINUTES)));
        return new JCacheCache(groupCache);

    }

    @Bean
    public Cache tokenInfoCache(javax.cache.CacheManager jCacheManager) {
        javax.cache.Cache groupCache = jCacheManager.createCache("tokenInfo",
                ExtendedMutableConfiguration.of(new Cache2kBuilder<String, AuthDto>(){}
                        .entryCapacity(10000)
                        .expireAfterWrite(1, TimeUnit.MINUTES)));
        return new JCacheCache(groupCache);

    }
}
