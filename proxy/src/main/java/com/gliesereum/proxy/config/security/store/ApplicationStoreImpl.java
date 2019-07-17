package com.gliesereum.proxy.config.security.store;

import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;
import com.gliesereum.share.common.security.application.filter.ApplicationStore;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Component
@Scope
@RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
public class ApplicationStoreImpl implements ApplicationStore {

    private ApplicationDto application;

    @Override
    public ApplicationDto getApplication() {
        return application;
    }

    @Override
    public void setApplication(ApplicationDto application) {
        this.application = application;
    }

    @Override
    public void clear() {
        this.application = null;
    }
}
