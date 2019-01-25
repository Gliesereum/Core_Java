package com.gliesereum.karma.model.repository.jpa.comment;

import com.gliesereum.karma.model.entity.comment.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {

    List<CommentEntity> findByObjectId(UUID objectId);

    boolean existsByObjectIdAndOwnerId(UUID objectId, UUID ownerId);

    CommentEntity findByIdAndOwnerId(UUID id, UUID ownerId);
}
