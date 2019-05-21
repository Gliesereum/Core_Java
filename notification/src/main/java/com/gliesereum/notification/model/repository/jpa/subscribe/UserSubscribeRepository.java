package com.gliesereum.notification.model.repository.jpa.subscribe;

import com.gliesereum.notification.model.entity.subscribe.UserSubscribeEntity;
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface UserSubscribeRepository extends JpaRepository<UserSubscribeEntity, UUID> {

    List<UserSubscribeEntity> findAllBySubscribeDestinationAndObjectId(SubscribeDestination subscribeDestination, UUID objectId);

    void deleteAllByUserDeviceId(UUID userDeviceId);

    List<UserSubscribeEntity> findAllByUserDeviceId(UUID userDeviceId);

    List<UserSubscribeEntity> findAllByUserDeviceIdInAndSubscribeDestinationAndNotificationEnableTrue(List<UUID> userDeviceIds, SubscribeDestination subscribeDestination);

    UserSubscribeEntity findByUserDeviceIdAndSubscribeDestinationAndObjectId(UUID userDeviceId, SubscribeDestination subscribeDestination, UUID objectId);

}