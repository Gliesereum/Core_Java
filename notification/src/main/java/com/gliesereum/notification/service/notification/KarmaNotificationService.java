package com.gliesereum.notification.service.notification;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.business.AbstractBusinessDto;
import com.gliesereum.share.common.model.dto.karma.chat.ChatMessageDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;
import com.gliesereum.share.common.model.dto.notification.notification.SendNotificationDto;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface KarmaNotificationService {

    void processRecordNotification(NotificationDto<BaseRecordDto> recordNotification);

    void processBusinessNotification(NotificationDto<AbstractBusinessDto> businessNotification);

    void processChatMessageNotification(NotificationDto<ChatMessageDto> chatMessageNotification);

    <T extends DefaultDto> void processNotification(NotificationDto<T> notification);

    void sendNotificationToUser(UUID userId, String title, String body);

    void sendBusinessNotification(SendNotificationDto sendNotification);
}
