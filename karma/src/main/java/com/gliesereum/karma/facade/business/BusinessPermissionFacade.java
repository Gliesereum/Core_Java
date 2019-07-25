package com.gliesereum.karma.facade.business;

import com.gliesereum.karma.model.common.BusinessPermission;

import java.util.Collection;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BusinessPermissionFacade {

    void checkPermissionByBusiness(UUID businessId, BusinessPermission businessPermission);
    boolean isHavePermissionByBusiness(UUID businessId, BusinessPermission businessPermission);

    void checkPermissionByBusiness(Collection<UUID> businessIds, BusinessPermission businessPermission);
    boolean isHavePermissionByBusiness(Collection<UUID> businessIds, BusinessPermission businessPermission);

    void checkPermissionByCorporation(UUID corporationId, BusinessPermission businessPermission);
    boolean isHavePermissionByCorporation(UUID corporationId, BusinessPermission businessPermission);

    boolean currentUserIsOwnerBusiness(UUID businessId);
    boolean currentUserIsWorkerBusiness(UUID businessId);

    boolean currentUserIdAdminBusiness(UUID businessId);


    boolean isOwner(UUID corporationId, UUID currentUserId, Collection<UUID> currentUserCorporationIds);
    boolean isWorker(UUID corporationId, UUID currentUserId);
    boolean isAdmin(UUID corporationId, UUID currentUserId);

    void checkCurrentUserPermissionToClient(UUID corporationId, UUID clientId);
}
