package com.gliesereum.mail.model.repository.jpa;

import com.gliesereum.mail.model.entity.FeedBackRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface FeedBackRequestRepository extends JpaRepository<FeedBackRequestEntity, UUID> {

    boolean existsByPhone(String phone);
}