package com.gliesereum.share.common.service.descrption.impl;

import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.base.description.BaseDescriptionDto;
import com.gliesereum.share.common.model.entity.description.BaseDescriptionEntity;
import com.gliesereum.share.common.repository.description.DescriptionRepository;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.service.descrption.DefaultDescriptionService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
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
    public List<D> create(List<D> description, UUID objectId) {
        if (CollectionUtils.isNotEmpty(description)) {
            description = description.stream()
                    .filter(i -> i.getLanguageCode() != null)
                    .peek(i -> i.setObjectId(objectId))
                    .peek(i -> i.setId(null))
                    .collect(Collectors.toList());
            description = super.create(description);
        }
        return description;
    }

    @Override
    @Transactional
    public List<D> update(List<D> description, UUID objectId) {
        List<D> result = null;
        if (objectId != null) {
            descriptionRepository.deleteAllByObjectId(objectId);
            result = create(description, objectId);
        }
        return result;
    }

    @Override
    @Transactional
    public void deleteByObjectId(UUID objectId) {
        if (objectId != null) {
            descriptionRepository.deleteAllByObjectId(objectId);
        }
    }
}
