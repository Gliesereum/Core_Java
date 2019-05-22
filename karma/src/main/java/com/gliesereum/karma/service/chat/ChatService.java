package com.gliesereum.karma.service.chat;

import com.gliesereum.karma.model.entity.chat.ChatEntity;
import com.gliesereum.share.common.model.dto.karma.chat.ChatDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ChatService extends DefaultService<ChatDto, ChatEntity> {

    List<ChatDto> getAllByUserId(UUID userId);

    ChatDto getByUserIdAndBusinessId(UUID userId, UUID businessId);

    List<ChatDto> getAllByBusinessId(UUID businessId);
}