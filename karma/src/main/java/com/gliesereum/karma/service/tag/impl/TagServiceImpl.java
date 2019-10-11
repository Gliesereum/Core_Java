package com.gliesereum.karma.service.tag.impl;

import com.gliesereum.karma.model.entity.tag.TagEntity;
import com.gliesereum.karma.model.repository.jpa.tag.TagRepository;
import com.gliesereum.karma.service.tag.TagService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.auditable.impl.AuditableServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.TAG_WITH_THIS_NAME_EXIST;


@Slf4j
@Service
public class TagServiceImpl extends AuditableServiceImpl<TagDto, TagEntity> implements TagService {

    private static final Class<TagDto> DTO_CLASS = TagDto.class;
    private static final Class<TagEntity> ENTITY_CLASS = TagEntity.class;

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository, DefaultConverter defaultConverter) {
        super(tagRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.tagRepository = tagRepository;
    }
    
    @Override
    public TagDto create(TagDto dto) {
        TagDto result = null;
        if (dto != null) {
            if (tagRepository.existsByNameAndObjectState(dto.getName(), ObjectState.ACTIVE)) {
                throw new ClientException(TAG_WITH_THIS_NAME_EXIST);
            }
            result = super.create(dto);
        }
        return result;
    }
    
    @Override
    public TagDto update(TagDto dto) {
        TagDto result = null;
        if (dto != null) {
            if (tagRepository.existsByNameAndIdNotAndObjectState(dto.getName(), dto.getId(), ObjectState.ACTIVE)) {
                throw new ClientException(TAG_WITH_THIS_NAME_EXIST);
            }
            result = super.update(dto);
        }
        return result;
    }
    
    @Override
    public Map<UUID, TagDto> getMapByIds(List<UUID> ids, List<ObjectState> states) {
        Map<UUID, TagDto> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            if(CollectionUtils.isEmpty(states)){
                states = Arrays.asList(ObjectState.ACTIVE);
            }
            List<TagEntity> entities = tagRepository.getAllByIdInAndObjectStateIn(ids, states);
            List<TagDto> tags = converter.convert(entities, dtoClass);


            if (CollectionUtils.isNotEmpty(tags)) {
                result = tags.stream().collect(Collectors.toMap(TagDto::getId, m -> m));
            }
        }
        return result;
    }
    
    @Override
    public List<TagDto> getTagsByName(List<String> names, List<ObjectState> states) {
        List<TagDto> result = null;
        if (CollectionUtils.isNotEmpty(names)) {
            List<TagEntity> entities = tagRepository.getAllByNameInAndObjectStateIn(names, states);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }
    
    @Override
    public List<TagDto> getByIds(List<UUID> tagIds) {
        List<TagDto> result = null;
        if (CollectionUtils.isNotEmpty(tagIds)) {
            List<TagEntity> tags = tagRepository.getAllByIdInAndObjectStateIn(tagIds, Arrays.asList(ObjectState.ACTIVE));
            result = converter.convert(tags, dtoClass);
        }
        return result;
    }
}