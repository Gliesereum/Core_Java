package com.gliesereum.karma.service.comment.impl;

import com.gliesereum.karma.model.entity.comment.CommentEntity;
import com.gliesereum.karma.model.repository.jpa.comment.CommentRepository;
import com.gliesereum.karma.service.comment.CommentService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */

@Service
public class CommentServiceImpl extends DefaultServiceImpl<CommentDto, CommentEntity> implements CommentService {

    private static final Class<CommentDto> DTO_CLASS = CommentDto.class;
    private static final Class<CommentEntity> ENTITY_CLASS = CommentEntity.class;

    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, DefaultConverter defaultConverter) {
        super(commentRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.commentRepository = commentRepository;
    }

    @Override
    public List<CommentDto> findByObjectId(UUID objectId) {
        List<CommentDto> result = null;
        if (objectId != null) {
            List<CommentEntity> entities = commentRepository.findByObjectId(objectId);
            result = converter.convert(entities, CommentDto.class);
        }
        return result;
    }

    @Override
    public CommentDto addComment(UUID objectId, UUID userId, CommentDto comment) {
        if (commentRepository.existsByObjectIdAndOwnerId(objectId, userId)) {
            throw new ClientException(COMMENT_FOR_USER_EXIST);
        }
        comment.setOwnerId(userId);
        comment.setObjectId(objectId);
        return create(comment);
    }

    @Override
    public CommentDto updateComment(UUID userId, CommentDto comment) {
        UUID id = comment.getId();
        if (id == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        CommentDto existed = super.getById(id);
        if (!existed.getOwnerId().equals(userId)) {
            throw new ClientException(CURRENT_USER_CANT_EDIT_THIS_COMMENT);
        }
        existed.setRating(comment.getRating());
        existed.setText(comment.getText());
        return update(existed);
    }

    @Override
    public void deleteComment(UUID commentId, UUID userId) {
        Optional<CommentEntity> commentOptional = commentRepository.findById(commentId);
        CommentEntity existed = commentOptional.orElseThrow(() -> new ClientException(COMMENT_NOT_FOUND));
        if (!existed.getOwnerId().equals(userId)) {
            throw new ClientException(CURRENT_USER_CANT_EDIT_THIS_COMMENT);
        }
        commentRepository.delete(existed);

    }
}
