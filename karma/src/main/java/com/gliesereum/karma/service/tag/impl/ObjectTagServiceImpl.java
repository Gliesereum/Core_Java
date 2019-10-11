package com.gliesereum.karma.service.tag.impl;

import com.gliesereum.karma.model.entity.tag.ObjectTagEntity;
import com.gliesereum.karma.model.repository.jpa.tag.ObjectTagRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.tag.ObjectTagService;
import com.gliesereum.karma.service.tag.TagService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.tag.ObjectTagDto;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.TAG_NOT_FOUND;


@Slf4j
@Service
public class ObjectTagServiceImpl extends DefaultServiceImpl<ObjectTagDto, ObjectTagEntity> implements ObjectTagService {

    private static final Class<ObjectTagDto> DTO_CLASS = ObjectTagDto.class;
    private static final Class<ObjectTagEntity> ENTITY_CLASS = ObjectTagEntity.class;

    private final ObjectTagRepository objectTagRepository;

    @Autowired
    private TagService tagService;

    @Autowired
    private BaseBusinessService businessService;

    @Autowired
    public ObjectTagServiceImpl(ObjectTagRepository objectTagRepository, DefaultConverter defaultConverter) {
        super(objectTagRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.objectTagRepository = objectTagRepository;
    }

    @Override
    public List<TagDto> addTag(UUID tagId, UUID objectId) {
        checkTagExist(tagId);
        ObjectTagEntity exist = objectTagRepository.getByObjectIdAndAndTagId(objectId, tagId);
        List<UUID> tagIds = getTagIdsByObjectId(objectId);
        if (exist == null) {
            tagIds.add(tagId);
            create(new ObjectTagDto(objectId, tagId));
        }
        return tagService.getByIds(tagIds);
    }

    @Override
    @Transactional
    public List<TagDto> saveTags(List<UUID> tagIds, UUID objectId) {
        List<TagDto> result = null;
        if (objectId != null) {
            objectTagRepository.deleteAllByObjectId(objectId);
            if (CollectionUtils.isNotEmpty(tagIds)) {
                List<ObjectTagDto> tags = tagIds.stream().map(i -> new ObjectTagDto(objectId, i)).collect(Collectors.toList());
                create(tags);
                result = tagService.getByIds(tagIds);
            }
        }
        return result;
    }

    @Override
    public List<TagDto> removeTag(UUID tagId, UUID objectId) {
        checkTagExist(tagId);
        ObjectTagEntity exist = objectTagRepository.getByObjectIdAndAndTagId(objectId, tagId);
        if (exist != null) {
            delete(exist.getId());
        }
        List<UUID> tagIds = getTagIdsByObjectId(objectId)
                .stream().filter(f -> !f.equals(tagId)).collect(Collectors.toList());
        return tagService.getByIds(tagIds);
    }

    @Override
    public List<UUID> getTagIdsByObjectId(UUID objectId) {
        List<UUID> result = new ArrayList<>();
        List<ObjectTagEntity> listExistTagByBusinessId = objectTagRepository.getByObjectId(objectId);
        if (CollectionUtils.isNotEmpty(listExistTagByBusinessId)) {
            result = listExistTagByBusinessId.stream().map(ObjectTagEntity::getTagId).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public void deleteByObjectId(UUID objectId) {
        if (objectId != null) {
            objectTagRepository.deleteAllByObjectId(objectId);
        }
    }

    @Override
    public List<TagDto> getByObjectId(UUID objectId) {
        List<TagDto> result = null;
        if (objectId != null) {
            List<ObjectTagEntity> businessTags = objectTagRepository.getByObjectId(objectId);
            if (CollectionUtils.isNotEmpty(businessTags)) {
                List<UUID> tagIds = businessTags.stream().map(ObjectTagEntity::getTagId).collect(Collectors.toList());
                result = tagService.getByIds(tagIds);
            }
        }
        return result;
    }

    @Override
    public Map<UUID, List<TagDto>> getMapByObjectIds(List<UUID> ids) {
        Map<UUID, List<TagDto>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            List<ObjectTagEntity> entities = objectTagRepository.getByObjectIdIn(ids);
            if (CollectionUtils.isNotEmpty(entities)) {
                Set<UUID> tagIds = entities.stream().map(ObjectTagEntity::getTagId).collect(Collectors.toSet());
                Map<UUID, TagDto> tags = tagService.getMapByIds(new ArrayList<>(tagIds), Arrays.asList(ObjectState.ACTIVE));
                if (MapUtils.isNotEmpty(tags)) {
                    result = entities.stream().collect(
                            Collectors.groupingBy(ObjectTagEntity::getObjectId, Collectors.mapping(i -> tags.get(i.getTagId()), Collectors.toList())));
                }
            }
        }
        return result;
    }

    private void checkTagExist(UUID tagId) {
        if (!tagService.isExist(tagId)) {
            throw new ClientException(TAG_NOT_FOUND);
        }
    }
}