package com.gliesereum.karma.service.business;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BusinessCategoryFacade {

    boolean currentUserHavePermissionToAction(UUID businessId);

    void throwExceptionIfUserDontHavePermissionToAction(UUID businessId);

    boolean currentUserHavePermissionToAction(UUID businessCategoryId, UUID businessId);

    void throwExceptionIfUserDontHavePermissionToAction(UUID businessCategoryId, UUID businessId);
}
