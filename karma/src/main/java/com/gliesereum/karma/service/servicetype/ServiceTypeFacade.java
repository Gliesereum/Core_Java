package com.gliesereum.karma.service.servicetype;

import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-13
 */
public interface ServiceTypeFacade {

    boolean currentUserHavePermissionToAction(ServiceType serviceType, UUID businessServiceId);

    void throwExceptionIfUserDontHavePermissionToAction(ServiceType serviceType, UUID businessServiceId);
}
