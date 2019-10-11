package com.gliesereum.notification.model.repository.jpa.telegram;

import com.gliesereum.notification.model.entity.telegram.TelegramChatActionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface TelegramChatActionRepository extends JpaRepository<TelegramChatActionEntity, UUID> {
	
	TelegramChatActionEntity findByChatId(Long chatId);
	
	void deleteAllByChatId(Long chatId);
}
