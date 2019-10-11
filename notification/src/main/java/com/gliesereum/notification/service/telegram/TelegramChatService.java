package com.gliesereum.notification.service.telegram;

import com.gliesereum.notification.model.entity.telegram.TelegramChatEntity;
import com.gliesereum.share.common.model.dto.notification.telegram.TelegramChatDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;


public interface TelegramChatService extends DefaultService<TelegramChatDto, TelegramChatEntity> {
	
	TelegramChatDto create(Long chatId, UUID userId);
	
	boolean existByChatId(Long chatId);
	
	List<TelegramChatDto> getByUserIds(List<UUID> userIds);
}
