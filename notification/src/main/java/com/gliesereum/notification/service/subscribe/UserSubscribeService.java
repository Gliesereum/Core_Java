package com.gliesereum.notification.service.subscribe;

import com.gliesereum.notification.model.entity.subscribe.UserSubscribeEntity;
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.dto.notification.subscribe.UserSubscribeDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface UserSubscribeService extends DefaultService<UserSubscribeDto, UserSubscribeEntity> {

    List<UserSubscribeDto> subscribe(List<UserSubscribeDto> subscribes, UUID userId, String registrationToken);

    List<UserSubscribeDto> getSubscribes(SubscribeDestination subscribeDestination, UUID objectId);

    List<UserSubscribeDto> getByUserDeviceId(UUID userDeviceId);

    List<UserSubscribeDto> getByRegistrationToken(String registrationToken);

    void unsubscribeAll(UUID deviceId, String registrationToke);

    void unsubscribe(UUID deviceId, String registrationToken, SubscribeDestination subscribeDestination, UUID objectId);
}