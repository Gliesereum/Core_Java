package com.gliesereum.karma.facade.notification;

import com.gliesereum.share.common.model.dto.karma.chat.ChatMessageDto;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface ChatNotificationFacade {

    void userMessageNotification(ChatMessageDto chatMessage, UUID userId);

    void businessMessageNotification(ChatMessageDto chatMessage, UUID businessId);
}
