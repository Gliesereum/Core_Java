package com.gliesereum.mail.model.repository.jpa;

import com.gliesereum.mail.model.entity.FeedBackUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface FeedBackUserRepository extends JpaRepository<FeedBackUserEntity, UUID> {

    List<FeedBackUserEntity> getAllByAppId(UUID id);

    boolean existsByAppIdAndUserId(UUID appId, UUID userId);
}