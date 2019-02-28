package com.gliesereum.lendinggallery.model.repository.jpa.account;

import com.gliesereum.lendinggallery.model.entity.account.MessageEntity;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.SectionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface MessageRepository extends JpaRepository<MessageEntity, UUID> {

    List<MessageEntity> findAllByCustomerIdOrderByCreate(UUID id);

    List<MessageEntity> findAllBySectionTypeInOrderByCreate(List<SectionType> types);
}