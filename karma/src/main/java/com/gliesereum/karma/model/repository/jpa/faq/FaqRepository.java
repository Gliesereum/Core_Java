package com.gliesereum.karma.model.repository.jpa.faq;

import com.gliesereum.karma.model.entity.faq.FaqEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface FaqRepository extends JpaRepository<FaqEntity, UUID> {

    List<FaqEntity> findByOrderByIndex();
}