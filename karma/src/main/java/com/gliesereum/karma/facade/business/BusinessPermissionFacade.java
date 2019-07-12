package com.gliesereum.karma.facade.business;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BusinessPermissionFacade {

    void checkUserHavePermissionToClient(UUID corporationId, UUID clientId,
                                         UUID currentUserId, List<UUID> currentUserCorporationIds);

    void checkCurrentUserHavePermissionToClient(UUID corporationId, UUID clientId);

    void checkUserHavePermissionForWorkWithClient(UUID corporationId, UUID currentUserId, List<UUID> currentUserCorporationIds);

    void checkCurrentUserHavePermissionForWorkWithClient(UUID corporationId);
}
