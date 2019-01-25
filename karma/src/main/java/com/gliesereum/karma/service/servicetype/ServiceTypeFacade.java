package com.gliesereum.karma.service.servicetype;

import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ServiceTypeFacade {

    boolean currentUserHavePermissionToAction(ServiceType serviceType, UUID corporationServiceId);

    void throwExceptionIfUserDontHavePermissionToAction(ServiceType serviceType, UUID corporationServiceId);
}
