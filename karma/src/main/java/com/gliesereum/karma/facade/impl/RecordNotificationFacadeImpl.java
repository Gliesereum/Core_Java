package com.gliesereum.karma.facade.impl;

import com.gliesereum.karma.facade.RecordNotificationFacade;
import com.gliesereum.karma.service.business.BaseBusinessService;
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

    @Value("${notification.notificationQueue}")
    private String notificationQueue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private BaseRecordService baseRecordService;

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Override
    @Async
    public void recordCreateNotification(AbstractRecordDto record) {
        if (record != null) {
            BaseRecordDto fullModel = baseRecordService.getFullModelById(record.getId());
            if (fullModel != null) {
                NotificationDto<BaseRecordDto> notification = new NotificationDto<>();
                notification.setData(fullModel);
                notification.setSubscribeDestination(SubscribeDestination.KARMA_BUSINESS_RECORD);
                notification.setObjectId(record.getBusinessId());
                rabbitTemplate.convertAndSend(notificationQueue, notification);

            }
        }
    }

    @Override
    @Async
    public void recordUpdateNotification(AbstractRecordDto record) {
        if (record != null) {
            BaseRecordDto fullModel = baseRecordService.getFullModelById(record.getId());
            if (fullModel != null) {
                UUID id = fullModel.getId();
                if (id != null) {
                    NotificationDto<BaseRecordDto> notification = new NotificationDto<>();
                    notification.setData(fullModel);
                    notification.setSubscribeDestination(SubscribeDestination.KARMA_USER_RECORD);
                    notification.setObjectId(id);
                    rabbitTemplate.convertAndSend(notificationQueue, notification);

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
        rabbitTemplate.convertAndSend(notificationQueue, notification);
        baseRecordService.setNotificationSend(record.getId());

    }

}
