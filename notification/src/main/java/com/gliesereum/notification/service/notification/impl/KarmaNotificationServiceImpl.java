package com.gliesereum.notification.service.notification.impl;

import com.gliesereum.notification.service.device.UserDeviceService;
import com.gliesereum.notification.service.firebase.FirebaseService;
import com.gliesereum.notification.service.notification.KarmaNotificationService;
import com.gliesereum.share.common.exchange.service.karma.KarmaExchangeService;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.business.AbstractBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.chat.ChatMessageDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceDto;
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;
import com.gliesereum.share.common.model.dto.notification.notification.SendNotificationDto;
import com.gliesereum.share.common.util.NotificationUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class KarmaNotificationServiceImpl implements KarmaNotificationService {

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private KarmaExchangeService karmaExchangeService;

    @Override
    public void processRecordNotification(NotificationDto<BaseRecordDto> recordNotification) {
        if (recordNotification != null) {
            SubscribeDestination subscribeDestination = recordNotification.getSubscribeDestination();
            String routingKey = NotificationUtil.routingKey(subscribeDestination.toString(), recordNotification.getObjectId());
            BaseRecordDto data = recordNotification.getData();
            StatusRecord statusRecord = data.getStatusRecord();
            StatusProcess statusProcess = data.getStatusProcess();
            firebaseService.sendNotificationToTopic(routingKey, getTitle(subscribeDestination, statusRecord, statusProcess), getBody(subscribeDestination, statusRecord, statusProcess), data.getId(), subscribeDestination);

        }
    }

    @Override
    public void processBusinessNotification(NotificationDto<AbstractBusinessDto> businessNotification) {
        processNotification(businessNotification);
    }

    @Override
    public <T extends DefaultDto> void processNotification(NotificationDto<T> notification) {
        if (notification != null) {
            SubscribeDestination subscribeDestination = notification.getSubscribeDestination();
            String routingKey = NotificationUtil.routingKey(subscribeDestination.toString(), notification.getObjectId());
            T data = notification.getData();
            firebaseService.sendNotificationToTopic(routingKey, getTitle(subscribeDestination), getBody(subscribeDestination), data.getId(), subscribeDestination);

        }
    }

    @Override
    public void processChatMessageNotification(NotificationDto<ChatMessageDto> notification) {
        if (notification != null) {
            SubscribeDestination subscribeDestination = notification.getSubscribeDestination();
            String routingKey = NotificationUtil.routingKey(subscribeDestination.toString(), notification.getObjectId());
            ChatMessageDto message = notification.getData();
            firebaseService.sendNotificationToTopic(routingKey, getTitle(subscribeDestination), message.getMessage(), message.getChatId(), subscribeDestination);
        }
    }

    @Override
    public void sendNotificationToUser(UUID userId, String title, String body) {
        if (ObjectUtils.allNotNull(userId, title, body)) {
            List<UserDeviceDto> byUserId = userDeviceService.getByUserId(userId);
            if (CollectionUtils.isNotEmpty(byUserId)) {
                List<String> tokens = byUserId.stream().map(UserDeviceDto::getFirebaseRegistrationToken).collect(Collectors.toList());
                firebaseService.sendNotificationToDevices(tokens, title, body, new HashMap<>());
            }
        }
    }

    @Override
    public void sendBusinessNotification(SendNotificationDto sendNotification) {
        if ((sendNotification != null) && (sendNotification.getDestination() != null)) {
            switch (sendNotification.getDestination()) {
                case KARMA_BUSINESS_NOTIFICATION: {
                    UUID businessId = sendNotification.getBusinessId();
                    List<BaseBusinessDto> business = karmaExchangeService.getBusinessForCurrentUser();
                    if (CollectionUtils.isNotEmpty(business) && business.stream().anyMatch(i -> i.getId().equals(businessId))) {
                        List<UserDeviceDto> devices = userDeviceService.getByUserIdsAndSubscribeExist(sendNotification.getUserIds(), sendNotification.getDestination());
                        if (CollectionUtils.isNotEmpty(devices)) {
                            List<String> tokens = devices.stream().map(UserDeviceDto::getFirebaseRegistrationToken).collect(Collectors.toList());
                            Map<String, String> data = new HashMap<>();
                            data.put("businessId", businessId.toString());
                            data.put("destination", sendNotification.getDestination().toString());
                            firebaseService.sendNotificationToDevices(tokens, sendNotification.getTitle(), sendNotification.getBody(), data);
                        }
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void sendSystemNotification(SendNotificationDto sendNotification) {
        String routingKey = NotificationUtil.routingKey(sendNotification.getDestination().toString(), sendNotification.getObjectId());
        firebaseService.sendNotificationToTopic(routingKey, sendNotification.getTitle(), sendNotification.getBody(), sendNotification.getObjectId(), sendNotification.getDestination(), sendNotification.getData());
    }
    
    private String getTitle(SubscribeDestination subscribeDestination, StatusRecord statusRecord, StatusProcess statusProcess) {
        String title;
        if (subscribeDestination.equals(SubscribeDestination.KARMA_USER_RECORD) && statusRecord.equals(StatusRecord.CREATED) && statusProcess.equals(StatusProcess.WAITING)) {
            title = messageSource.getMessage(subscribeDestination.toString() + '.' + statusRecord.name() + '.' + "title", new Object[]{}, Locale.getDefault());
        } else {
            title = messageSource.getMessage(subscribeDestination.toString() + '.' + "title", new Object[]{}, Locale.getDefault());
        }
        return title;
    }

    private String getBody(SubscribeDestination subscribeDestination, StatusRecord statusRecord, StatusProcess statusProcess) {
        String body;
        if (subscribeDestination.equals(SubscribeDestination.KARMA_USER_RECORD) && statusRecord.equals(StatusRecord.CREATED) && statusProcess.equals(StatusProcess.WAITING)) {
            body = messageSource.getMessage(subscribeDestination.toString() + '.' + statusRecord.name() + '.' + "body", new Object[]{}, Locale.getDefault());
        } else {
            body = messageSource.getMessage(subscribeDestination.toString() + '.' + "body", new Object[]{}, Locale.getDefault());
        }
        return body;
    }

    private String getTitle(SubscribeDestination subscribeDestination) {
        return messageSource.getMessage(subscribeDestination.toString() + '.' + "title", new Object[]{}, Locale.getDefault());
    }

    private String getBody(SubscribeDestination subscribeDestination) {
        return messageSource.getMessage(subscribeDestination.toString() + '.' + "body", new Object[]{}, Locale.getDefault());
    }
}
