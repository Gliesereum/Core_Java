package com.gliesereum.notification.service.telegram;

import com.gliesereum.notification.model.entity.telegram.TelegramChatActionEntity;
import com.gliesereum.share.common.model.dto.notification.enumerated.TelegramAction;
import com.gliesereum.share.common.model.dto.notification.telegram.TelegramChatActionDto;
import com.gliesereum.share.common.service.DefaultService;


public interface TelegramChatActionService extends DefaultService<TelegramChatActionDto, TelegramChatActionEntity> {
	
	TelegramChatActionDto getByChatId(Long chatId);
	
	TelegramChatActionDto create(Long chatId, TelegramAction action);
	
	TelegramChatActionDto create(Long chatId, TelegramAction action, String value);
	
	void deleteByChatId(Long chatId);
}
