package com.gliesereum.notification.mq;

import com.gliesereum.notification.bot.NotificationTelegramBotService;
import com.gliesereum.notification.service.notification.NotificationService;
import com.gliesereum.share.common.model.dto.karma.business.AbstractBusinessDto;
import com.gliesereum.share.common.model.dto.karma.chat.ChatMessageDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordNotificationDto;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Component
@Slf4j
public class NotificationMqListener {

    @Autowired
    private NotificationService notificationService;
    
    @Autowired
    private NotificationTelegramBotService notificationTelegramBotService;

    @RabbitListener(queuesToDeclare = @Queue(name = "${notification.record.queueName}", ignoreDeclarationExceptions = "true"))
    public void receiveRecordNotification(NotificationDto<BaseRecordDto> recordNotification) {
        try {
            notificationService.processRecordNotification(recordNotification);
        } catch (Exception e) {
            log.warn("Error while receive record notification", e);
        }
    }
    
    @RabbitListener(queuesToDeclare = @Queue(name = "${notification.create-record.queueName}", ignoreDeclarationExceptions = "true"))
    public void receiveCreateRecordNotification(NotificationDto<RecordNotificationDto> recordNotification) {
        try {
            NotificationDto<BaseRecordDto> notification = new NotificationDto<>();
            notification.setObjectId(recordNotification.getObjectId());
            notification.setData(recordNotification.getData().getRecord());
            notification.setSubscribeDestination(recordNotification.getSubscribeDestination());
            notificationService.processRecordNotification(notification);
            
        } catch (Exception e) {
            log.warn("Error while receive record notification", e);
        }
        try {
            notificationTelegramBotService.recordCreateNotification(recordNotification.getData());
        } catch (Exception e) {
            log.warn("Error while send notification via telegram", e);
        }
    }

    @RabbitListener(queuesToDeclare = @Queue(name = "${notification.business.queueName}", ignoreDeclarationExceptions = "true"))
    public void receiveBusinessNotification(NotificationDto<AbstractBusinessDto> businessNotification) {
        try {
            notificationService.processBusinessNotification(businessNotification);
        } catch (Exception e) {
            log.warn("Error while receive business notification", e);
        }
    }

    @RabbitListener(queuesToDeclare = @Queue(name = "${notification.chat-message.queueName}", ignoreDeclarationExceptions = "true"))
    public void receiveChatMessageNotification(NotificationDto<ChatMessageDto> chatMessageNotification) {
        try {
            notificationService.processChatMessageNotification(chatMessageNotification);
        } catch (Exception e) {
            log.warn("Error while receive chat message notification", e);
        }
    }
}
