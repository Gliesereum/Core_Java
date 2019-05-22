package com.gliesereum.karma.model.repository.jpa.chat;

import com.gliesereum.karma.model.entity.chat.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, UUID> {

    List<ChatMessageEntity> getAllByChatId(UUID chatId);
}