package com.gliesereum.karma.facade.notification.impl;

import com.gliesereum.karma.facade.notification.ChatNotificationFacade;
import com.gliesereum.share.common.model.dto.karma.chat.ChatMessageDto;
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;
import org.apache.commons.lang3.ObjectUtils;
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
public class ChatNotificationFacadeImpl implements ChatNotificationFacade {

    @Value("${notification.chat-message.queueName}")
    private String notificationChatMessageQueue;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Async
    @Override
    public void userMessageNotification(ChatMessageDto chatMessage, UUID userId) {
        if (ObjectUtils.allNotNull(chatMessage, userId)) {
            NotificationDto<ChatMessageDto> notification = new NotificationDto<>();
            notification.setData(chatMessage);
            notification.setObjectId(userId);
            notification.setSubscribeDestination(SubscribeDestination.KARMA_CHAT_USER);
            rabbitTemplate.convertAndSend(notificationChatMessageQueue, notification);
        }

    }

    @Async
    @Override
    public void businessMessageNotification(ChatMessageDto chatMessage, UUID businessId) {
        if (ObjectUtils.allNotNull(chatMessage, businessId)) {
            NotificationDto<ChatMessageDto> notification = new NotificationDto<>();
            notification.setData(chatMessage);
            notification.setObjectId(businessId);
            notification.setSubscribeDestination(SubscribeDestination.KARMA_CHAT_BUSINESS);
            rabbitTemplate.convertAndSend(notificationChatMessageQueue, notification);
        }
    }
}
