package com.gliesereum.karma.model.repository.jpa.chat;

import com.gliesereum.karma.model.entity.chat.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ChatRepository extends JpaRepository<ChatEntity, UUID> {

    List<ChatEntity> getAllByUserId(UUID userId);

    List<ChatEntity> getAllByBusinessId(UUID businessId);

    ChatEntity getByUserIdAndBusinessId(UUID userId, UUID businessId);
}