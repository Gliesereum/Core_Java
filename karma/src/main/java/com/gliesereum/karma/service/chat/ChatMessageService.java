package com.gliesereum.karma.service.chat;

import com.gliesereum.karma.model.entity.chat.ChatMessageEntity;
import com.gliesereum.share.common.model.dto.karma.chat.ChatMessageDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ChatMessageService extends DefaultService<ChatMessageDto, ChatMessageEntity> {
    
    List<ChatMessageDto> getAllByChatId(UUID id);

    ChatMessageDto createByBusinessId(UUID id, String message);

    ChatMessageDto createByChatId(UUID id, String message);
}    