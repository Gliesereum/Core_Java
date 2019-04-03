package com.gliesereum.mail.model.repository.jpa;

import com.gliesereum.mail.model.entity.MailStateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface MailStateRepository extends JpaRepository<MailStateEntity, UUID> {

    List<MailStateEntity> findAllByMessageStatusAndCreateAfter(Integer messageStatus, LocalDateTime date);
}