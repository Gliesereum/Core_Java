package com.gliesereum.karma.facade.business;

import java.util.Collection;
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

    void checkUserHavePermissionForWorkWithBusinessClient(List<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds);
    void checkCurrentUserHavePermissionForWorkWithBusinessClient(List<UUID> businessIds);

    boolean checkUserHavePermissionToViewClientPhone(UUID corporationId, UUID currentUserId, List<UUID> currentUserCorporationIds);
    boolean checkCurrentUserHavePermissionToViewClientPhone(UUID corporationId);

    boolean checkUserHavePermissionToViewBusinessClientPhone(Collection<UUID> businessIds, UUID currentUserId, List<UUID> currentUserCorporationIds);
    boolean checkCurrentUserHavePermissionToBusinessViewClientPhone(Collection<UUID> businessIds);

}
