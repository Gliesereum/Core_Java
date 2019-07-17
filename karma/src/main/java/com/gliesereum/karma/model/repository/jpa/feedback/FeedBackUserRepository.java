package com.gliesereum.karma.model.repository.jpa.feedback;

import com.gliesereum.karma.model.entity.feedback.FeedBackUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface FeedBackUserRepository extends JpaRepository<FeedBackUserEntity, UUID>, FeedBackSearchRepository {

    FeedBackUserEntity findByObjectId(UUID objectId);
}