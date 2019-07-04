package com.gliesereum.proxy.facade.impl;

import com.gliesereum.proxy.facade.CacheFacade;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.gliesereum.proxy.config.CacheConfiguration.TOKEN_INFO_CACHE;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class CacheFacadeImpl implements CacheFacade {

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void updateAuthCache(List<AuthDto> authInfo) {
        if (CollectionUtils.isNotEmpty(authInfo)) {
            Cache tokenInfoCache = cacheManager.getCache(TOKEN_INFO_CACHE);
            if (tokenInfoCache != null) {
                authInfo.forEach(i -> tokenInfoCache.put(i.getTokenInfo().getAccessToken(), i));
            }
        }
    }
}
