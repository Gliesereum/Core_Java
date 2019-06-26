package com.gliesereum.karma.model.repository.jpa.record;

import com.gliesereum.karma.model.entity.record.RecordMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface RecordMessageRepository extends JpaRepository<RecordMessageEntity, UUID> {
}