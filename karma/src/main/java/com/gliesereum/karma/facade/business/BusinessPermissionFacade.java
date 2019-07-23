package com.gliesereum.karma.facade.business;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BusinessPermissionFacade {

    boolean currentUserIsOwnerBusiness(Collection<UUID> businessIds);
    boolean isOwnerBusiness(Collection<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds);
    boolean currentUserIsOwnerBusiness(UUID businessId);
    void checkCurrentUserIsOwnerBusiness(UUID businessId);
    boolean isOwnerBusiness(UUID businessId, UUID currentUserId, List<UUID> currentUserCorporationIds);
    boolean isOwner(UUID corporationId, UUID currentUserId, Collection<UUID> currentUserCorporationIds);

    boolean isWorkerBusiness(UUID businessId, UUID userId);
    boolean currentUserIsWorkerBusiness(UUID businessId);
    boolean isWorker(UUID corporationId, UUID currentUserId);
    boolean currentUserIsWorker(UUID corporationId);

    void checkPermissionToBusinessInfo(UUID businessId, UUID currentUserId, List<UUID> currentUserCorporationIds);
    void checkCurrentUserPermissionToBusinessInfo(UUID businessId);

    void checkPermissionToBusinessInfo(List<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds);
    void checkCurrentUserPermissionToBusinessInfo(List<UUID> businessIds);

    void checkPermissionToClient(UUID corporationId, UUID clientId, UUID currentUserId, List<UUID> currentUserCorporationIds);
    void checkCurrentUserPermissionToClient(UUID corporationId, UUID clientId);

    void checkPermissionWorkWithClient(UUID corporationId, UUID currentUserId, List<UUID> currentUserCorporationIds);
    void checkCurrentUserPermissionWorkWithClient(UUID corporationId);

    void checkPermissionWorkWithClientByBusiness(List<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds);
    void checkCurrentUserPermissionWorkWithClientByBusiness(List<UUID> businessIds);

    boolean checkPermissionViewClientPhone(UUID corporationId, UUID currentUserId, List<UUID> currentUserCorporationIds);
    boolean checkCurrentUserPermissionViewClientPhone(UUID corporationId);

    boolean checkPermissionViewClientPhoneByBusiness(Collection<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds);
    boolean checkCurrentUserPermissionViewClientPhoneByBusiness(Collection<UUID> businessIds);

}
