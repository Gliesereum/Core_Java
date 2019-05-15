package com.gliesereum.notification.service.notification;

import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface NotificationService {

    void processRecordNotification(NotificationDto<BaseRecordDto> recordNotification);

    void sendNotificationToUser(UUID userId, String title, String body);
}
