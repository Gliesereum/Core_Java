package com.gliesereum.notification.model.repository.jpa.telegram;

import com.gliesereum.notification.model.entity.telegram.TelegramChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface TelegramChatRepository extends JpaRepository<TelegramChatEntity, UUID> {
	
	boolean existsByUserIdAndChatId(UUID userId, Long chatId);
	
	boolean existsByChatId(Long chatId);
	
	List<TelegramChatEntity> findByUserIdIn(List<UUID> userIds);
}
