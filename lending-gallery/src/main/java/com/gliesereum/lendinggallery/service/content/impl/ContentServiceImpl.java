package com.gliesereum.lendinggallery.service.content.impl;

import com.gliesereum.lendinggallery.model.entity.content.ContentEntity;
import com.gliesereum.lendinggallery.model.repository.jpa.content.ContentRepository;
import com.gliesereum.lendinggallery.service.content.ContentService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.lendinggallery.content.ContentDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.ContentType;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class ContentServiceImpl extends DefaultServiceImpl<ContentDto, ContentEntity> implements ContentService {

    private static final Class<ContentDto> DTO_CLASS = ContentDto.class;
    private static final Class<ContentEntity> ENTITY_CLASS = ContentEntity.class;

    private final ContentRepository contentRepository;

    @Autowired
    public ContentServiceImpl(ContentRepository contentRepository, DefaultConverter defaultConverter) {
        super(contentRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.contentRepository = contentRepository;
    }

    @Override
    public List<ContentDto> getAllByContentType(ContentType type, Integer page, Integer size) {
        List<ContentEntity> entities = contentRepository.findAllByContentTypeOrderByCreate(type, PageRequest.of(page, size));
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<ContentDto> getAllByTags(List<String> tags, Integer page, Integer size) {
        if (CollectionUtils.isEmpty(tags)) {
            return new ArrayList<>();
        }
        List<ContentEntity> entities = contentRepository.findAllByTagsContainsOrderByCreate(tags, PageRequest.of(page, size));
        return converter.convert(entities, dtoClass);
    }
}
