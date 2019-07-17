package com.gliesereum.share.common.security.application.filter;

import com.gliesereum.share.common.model.dto.permission.application.ApplicationDto;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ApplicationStore {

    void setApplication(ApplicationDto application);

    ApplicationDto getApplication();

    void clear();

}
