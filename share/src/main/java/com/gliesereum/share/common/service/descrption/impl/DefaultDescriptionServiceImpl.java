package com.gliesereum.share.common.service.descrption.impl;

import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.base.description.BaseDescriptionDto;
import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.LanguageCode;
import com.gliesereum.share.common.model.entity.description.BaseDescriptionEntity;
import com.gliesereum.share.common.repository.description.DescriptionRepository;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.service.descrption.DefaultDescriptionService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public abstract class DefaultDescriptionServiceImpl<D extends BaseDescriptionDto, E extends BaseDescriptionEntity> extends DefaultServiceImpl<D, E> implements DefaultDescriptionService<D, E> {

    private final DescriptionRepository<E> descriptionRepository;

    public DefaultDescriptionServiceImpl(DescriptionRepository<E> descriptionRepository, DefaultConverter defaultConverter, Class<D> dtoClass, Class<E> entityClass) {
        super(descriptionRepository, defaultConverter, dtoClass, entityClass);
        this.descriptionRepository = descriptionRepository;
    }

    @Override
    public DescriptionReadableDto<D> create(DescriptionReadableDto<D> description, UUID objectId) {
        DescriptionReadableDto<D> result = null;
        if (MapUtils.isNotEmpty(description) && (objectId != null)) {
            Set<Map.Entry<LanguageCode, D>> entries = description.entrySet();
            if (CollectionUtils.isNotEmpty(entries)) {
                List<D> dtos = entries.stream()
                        .filter(i -> ObjectUtils.allNotNull(i.getKey(), i.getValue()))
                        .peek(i -> i.getValue().setLanguageCode(i.getKey()))
                        .map(Map.Entry::getValue)
                        .filter(i -> i.getLanguageCode() != null)
                        .peek(i -> i.setObjectId(objectId))
                        .peek(i -> i.setId(null))
                        .collect(Collectors.toList());
                dtos = super.create(dtos);
                if (CollectionUtils.isNotEmpty(dtos)) {
                    Map<LanguageCode, D> map = dtos.stream()
                            .collect(Collectors.toMap(BaseDescriptionDto::getLanguageCode, i -> i));
                    result = new DescriptionReadableDto<>(map);
                }
            }
        }
        return result;
    }

    @Override
    @Transactional
    public DescriptionReadableDto<D> update(DescriptionReadableDto<D> description, UUID objectId) {
        DescriptionReadableDto<D> result = null;
        if (objectId != null) {
            descriptionRepository.deleteAllByObjectId(objectId);
            result = create(description, objectId);
        }
        return result;
    }
}
