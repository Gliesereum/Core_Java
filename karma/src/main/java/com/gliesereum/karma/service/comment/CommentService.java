package com.gliesereum.karma.service.comment;

import com.gliesereum.karma.model.entity.comment.CommentEntity;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.model.dto.karma.comment.CommentFullDto;
import com.gliesereum.share.common.model.dto.karma.comment.RatingDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CommentService extends DefaultService<CommentDto, CommentEntity> {

    List<CommentDto> findByObjectId(UUID objectId);

    List<CommentFullDto> findFullByObjectId(UUID objectId);

    CommentDto addComment(UUID objectId, UUID userId, CommentDto comment);

    CommentDto updateComment(UUID userId, CommentDto comment);

    void deleteComment(UUID commentId, UUID userId);

    RatingDto getRating(UUID objectId);
}
