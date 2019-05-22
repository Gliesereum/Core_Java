package com.gliesereum.karma.model.repository.jpa.chat;

import com.gliesereum.karma.model.entity.chat.ChatSupportEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ChatSupportRepository extends JpaRepository<ChatSupportEntity, UUID> {

    boolean existsByBusinessIdAndUserId(UUID businessId, UUID userId);

    List<ChatSupportEntity> getAllByBusinessId(UUID businessId);
}