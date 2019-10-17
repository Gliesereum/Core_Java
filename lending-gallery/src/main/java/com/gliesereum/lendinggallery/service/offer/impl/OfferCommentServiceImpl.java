package com.gliesereum.lendinggallery.service.offer.impl;

import com.gliesereum.lendinggallery.model.entity.offer.OfferCommentEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.offer.OfferCommentRepository;
import com.gliesereum.lendinggallery.service.offer.OfferCommentService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.OfferCommentDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Slf4j
@Service
public class OfferCommentServiceImpl extends DefaultServiceImpl<OfferCommentDto, OfferCommentEntity> implements OfferCommentService {

    private static final Class<OfferCommentDto> DTO_CLASS = OfferCommentDto.class;
    private static final Class<OfferCommentEntity> ENTITY_CLASS = OfferCommentEntity.class;

    private final OfferCommentRepository offerCommentRepository;

    @Autowired
    public OfferCommentServiceImpl(OfferCommentRepository offerCommentRepository, DefaultConverter defaultConverter) {
        super(offerCommentRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.offerCommentRepository = offerCommentRepository;
    }

}