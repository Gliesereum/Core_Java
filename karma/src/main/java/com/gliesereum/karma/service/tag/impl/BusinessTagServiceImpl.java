package com.gliesereum.karma.service.tag.impl;

import com.gliesereum.karma.model.entity.tag.BusinessTagEntity;
import com.gliesereum.karma.model.repository.jpa.tag.BusinessTagRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.tag.BusinessTagService;
import com.gliesereum.karma.service.tag.TagService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.tag.BusinessTagDto;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.BUSINESS_NOT_FOUND;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.TAG_NOT_FOUND;


@Slf4j
@Service
public class BusinessTagServiceImpl extends DefaultServiceImpl<BusinessTagDto, BusinessTagEntity> implements BusinessTagService {

    private static final Class<BusinessTagDto> DTO_CLASS = BusinessTagDto.class;
    private static final Class<BusinessTagEntity> ENTITY_CLASS = BusinessTagEntity.class;

    private final BusinessTagRepository businessTagRepository;
    
    @Autowired
    private TagService tagService;
    
    @Autowired
    private BaseBusinessService businessService;

    @Autowired
    public BusinessTagServiceImpl(BusinessTagRepository businessTagRepository, DefaultConverter defaultConverter) {
        super(businessTagRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.businessTagRepository = businessTagRepository;
    }

    @Override
    public List<TagDto> addTag(UUID tagId, UUID businessId) {
        checkExist(tagId, businessId);
        BusinessTagEntity exist = businessTagRepository.getByBusinessIdAndAndTagId(businessId, tagId);
        List<UUID> tagIds = getTagIdsByBusinessId(businessId);
        if (exist == null) {
            tagIds.add(tagId);
            create(new BusinessTagDto(businessId, tagId));
        }
        return tagService.getByIds(tagIds);
    }

    @Override
    public List<TagDto> removeTag(UUID tagId, UUID businessId) {
        checkExist(tagId, businessId);
        BusinessTagEntity exist = businessTagRepository.getByBusinessIdAndAndTagId(businessId, tagId);
        if (exist != null) {
            delete(exist.getId());
        }
        List<UUID> tagIds = getTagIdsByBusinessId(businessId)
                .stream().filter(f -> !f.equals(tagId)).collect(Collectors.toList());
        return tagService.getByIds(tagIds);
    }

    @Override
    public List<UUID> getTagIdsByBusinessId(UUID businessId) {
        List<UUID> result = new ArrayList<>();
        List<BusinessTagEntity> listExistTagByBusinessId = businessTagRepository.getByBusinessId(businessId);
        if (CollectionUtils.isNotEmpty(listExistTagByBusinessId)) {
            result = listExistTagByBusinessId.stream().map(BusinessTagEntity::getTagId).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public void deleteByBusinessId(UUID businessId) {
        if (businessId != null) {
            businessTagRepository.deleteAllByBusinessId(businessId);
        }
    }
    
    @Override
    public List<TagDto> getByBusinessId(UUID businessId) {
        List<TagDto> result = null;
        if (businessId != null) {
            List<BusinessTagEntity> businessTags = businessTagRepository.getByBusinessId(businessId);
            if (CollectionUtils.isNotEmpty(businessTags)) {
                List<UUID> tagIds = businessTags.stream().map(BusinessTagEntity::getTagId).collect(Collectors.toList());
                result = tagService.getByIds(tagIds);
            }
        }
        return result;
    }
    
    @Override
    public Map<UUID, List<TagDto>> getMapByBusinessIds(List<UUID> ids) {
        Map<UUID, List<TagDto>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            List<BusinessTagEntity> entities = businessTagRepository.getByBusinessIdIn(ids);
            if (CollectionUtils.isNotEmpty(entities)) {
                Set<UUID> tagIds = entities.stream().map(BusinessTagEntity::getTagId).collect(Collectors.toSet());
                Map<UUID, TagDto> tags = tagService.getMapByIds(new ArrayList<>(tagIds), Arrays.asList(ObjectState.ACTIVE));
                if (MapUtils.isNotEmpty(tags)) {
                    result = entities.stream().collect(
                            Collectors.groupingBy(BusinessTagEntity::getBusinessId, Collectors.mapping(i -> tags.get(i.getTagId()), Collectors.toList())));
                }
            }
        }
        return result;
    }

    private void checkExist(UUID tagId, UUID businessId) {
        if (!businessService.isExist(businessId)) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        if (!tagService.isExist(tagId)) {
            throw new ClientException(TAG_NOT_FOUND);
        }
    }
}