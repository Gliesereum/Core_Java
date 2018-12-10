package com.gliesereum.karma.service.comment.impl;

import com.gliesereum.karma.model.entity.comment.CommentEntity;
import com.gliesereum.karma.model.repository.jpa.comment.CommentRepository;
import com.gliesereum.karma.service.comment.CommentService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.comment.CommentDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */

@Service
public class CommentServiceImpl extends DefaultServiceImpl<CommentDto, CommentEntity> implements CommentService {

    private static final Class<CommentDto> DTO_CLASS = CommentDto.class;
    private static final Class<CommentEntity> ENTITY_CLASS = CommentEntity.class;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, DefaultConverter defaultConverter) {
        super(commentRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }
}
