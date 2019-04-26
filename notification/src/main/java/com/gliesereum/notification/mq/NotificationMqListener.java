package com.gliesereum.notification.mq;

import com.gliesereum.notification.service.notification.NotificationService;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
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

    @RabbitListener(queuesToDeclare = @Queue(name = "${notification.queueName}", ignoreDeclarationExceptions = "true"))
    public void receiveNotification(NotificationDto<BaseRecordDto> baseRecordDto) {
        try {
            notificationService.processRecordNotification(baseRecordDto);
        } catch (Exception e) {
            log.warn("Error while send notification", e);
        }
    }
}
