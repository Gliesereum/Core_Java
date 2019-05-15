package com.gliesereum.notification.service.notification.impl;

import com.gliesereum.notification.service.device.UserDeviceService;
import com.gliesereum.notification.service.firebase.FirebaseService;
import com.gliesereum.notification.service.notification.NotificationService;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceDto;
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;
import com.gliesereum.share.common.util.NotificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDeviceService userDeviceService;

    @Override
    public void processRecordNotification(NotificationDto<BaseRecordDto> recordNotification) {
        if (recordNotification != null) {
            String routingKey = NotificationUtil.routingKey(recordNotification.getSubscribeDestination().toString(), recordNotification.getObjectId());
            BaseRecordDto data = recordNotification.getData();
            SubscribeDestination subscribeDestination = recordNotification.getSubscribeDestination();
            firebaseService.sendNotificationToTopic(routingKey, getTitle(subscribeDestination), getBody(subscribeDestination), data.getId(), subscribeDestination);

        }
    }

    @Override
    public void sendNotificationToUser(UUID userId, String title, String body) {
        if (ObjectUtils.allNotNull(userId, title, body)) {
            List<UserDeviceDto> byUserId = userDeviceService.getByUserId(userId);
            if (CollectionUtils.isNotEmpty(byUserId)) {
                byUserId.forEach(userDevice -> firebaseService.sendNotificationToDevice(userDevice.getFirebaseRegistrationToken(), title, body));
            }
        }
    }

    private String getTitle(SubscribeDestination subscribeDestination) {
        return messageSource.getMessage(subscribeDestination.toString() + '.' + "title", new Object[]{}, Locale.getDefault());
    }

    private String getBody(SubscribeDestination subscribeDestination) {
        return messageSource.getMessage(subscribeDestination.toString() + '.' + "body", new Object[]{}, Locale.getDefault());
    }
}
