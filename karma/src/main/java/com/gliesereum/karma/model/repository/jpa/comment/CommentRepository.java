package com.gliesereum.karma.model.repository.jpa.comment;

import com.gliesereum.karma.model.entity.comment.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {

    List<CommentEntity> findByObjectIdOrderByDateCreatedDesc(UUID objectId);

    List<CommentEntity> findByObjectId(UUID objectId);

    List<CommentEntity> findAllByObjectIdIn(List<UUID> objectIds);

    boolean existsByObjectIdAndOwnerId(UUID objectId, UUID ownerId);

    CommentEntity findByIdAndOwnerId(UUID id, UUID ownerId);

    CommentEntity findByObjectIdAndOwnerId(UUID objectId, UUID ownerId);
}
