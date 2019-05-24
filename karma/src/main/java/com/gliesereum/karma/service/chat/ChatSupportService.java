package com.gliesereum.karma.service.chat;

import com.gliesereum.karma.model.entity.chat.ChatSupportEntity;
import com.gliesereum.share.common.model.dto.karma.chat.ChatSupportDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ChatSupportService extends DefaultService<ChatSupportDto, ChatSupportEntity> {

    List<ChatSupportDto> getByBusinessId(UUID businessId);

    boolean existChatSupport(UUID userId, UUID businessId);
}    