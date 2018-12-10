package com.gliesereum.karma.model.repository.jpa.comment;

import com.gliesereum.karma.model.entity.comment.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
}
