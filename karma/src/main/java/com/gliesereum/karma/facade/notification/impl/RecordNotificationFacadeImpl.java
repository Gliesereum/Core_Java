package com.gliesereum.karma.facade.notification.impl;

import com.gliesereum.karma.facade.notification.RecordNotificationFacade;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.share.common.model.dto.karma.record.AbstractRecordDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class RecordNotificationFacadeImpl implements RecordNotificationFacade {

    @Value("${notification.record.queueName}")
    private String notificationRecordQueue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BaseRecordService baseRecordService;

    @Override
    @Async
    public void recordBusinessNotification(BaseRecordDto record) {
        if (record != null) {
            NotificationDto<AbstractRecordDto> notification = new NotificationDto<>();
            notification.setData(record);
            notification.setSubscribeDestination(SubscribeDestination.KARMA_BUSINESS_RECORD);
            notification.setObjectId(record.getBusinessId());
            rabbitTemplate.convertAndSend(notificationRecordQueue, notification);
        }
    }

    @Override
    @Async
    public void recordUpdateNotification(BaseRecordDto record) {
        if (record != null) {
            BaseRecordDto fullModel = baseRecordService.getFullModelById(record.getId());
            if (fullModel != null) {
                UUID id = fullModel.getClientId();
                if (id != null) {
                    NotificationDto<BaseRecordDto> notification = new NotificationDto<>();
                    notification.setData(fullModel);
                    notification.setSubscribeDestination(SubscribeDestination.KARMA_USER_RECORD);
                    notification.setObjectId(id);
                    rabbitTemplate.convertAndSend(notificationRecordQueue, notification);
                }
            }
        }
    }

    @Override
    @Async
    public void recordRemindNotification(BaseRecordDto record) {
        NotificationDto<BaseRecordDto> notification = new NotificationDto<>();
        notification.setData(record);
        notification.setSubscribeDestination(SubscribeDestination.KARMA_USER_REMIND_RECORD);
        notification.setObjectId(record.getClientId());
        rabbitTemplate.convertAndSend(notificationRecordQueue, notification);
        baseRecordService.setNotificationSend(record.getId());
    }
}
