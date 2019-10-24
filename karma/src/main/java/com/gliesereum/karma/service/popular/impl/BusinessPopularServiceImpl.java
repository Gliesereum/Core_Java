package com.gliesereum.karma.service.popular.impl;

import com.gliesereum.karma.model.entity.popular.BusinessPopularEntity;
import com.gliesereum.karma.model.repository.jpa.popular.BusinessPopularRepository;
import com.gliesereum.karma.service.popular.BusinessPopularService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.business.popular.BusinessPopularDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class BusinessPopularServiceImpl extends DefaultServiceImpl<BusinessPopularDto, BusinessPopularEntity> implements BusinessPopularService {
    
    private static final Class<BusinessPopularDto> DTO_CLASS = BusinessPopularDto.class;
    private static final Class<BusinessPopularEntity> ENTITY_CLASS = BusinessPopularEntity.class;
    
    private final BusinessPopularRepository businessPopularRepository;
    
    @Autowired
    public BusinessPopularServiceImpl(BusinessPopularRepository businessPopularRepository, DefaultConverter defaultConverter) {
        super(businessPopularRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.businessPopularRepository = businessPopularRepository;
    }
    
    @Override
    @Async
    public void updateBusinessCountAsync(UUID businessId) {
        if (businessId != null) {
            BusinessPopularEntity entity = businessPopularRepository.findByBusinessId(businessId);
            if (entity == null) {
                entity = new BusinessPopularEntity();
                entity.setBusinessId(businessId);
                entity.setCount(0L);
            }
            entity.setCount(entity.getCount() + 1);
            repository.saveAndFlush(entity);
        }
    }
    
    @Override
    @Async
    public void updateBusinessCountAsync(List<UUID> businessIds) {
        if (CollectionUtils.isNotEmpty(businessIds)) {
            businessIds.forEach(this::updateBusinessCountAsync);
        }
    }
    
    @Override
    public List<BusinessPopularDto> getByBusinessIds(Iterable<UUID> businessIds) {
        List<BusinessPopularDto> result = null;
        if (!IterableUtils.isEmpty(businessIds)) {
            List<BusinessPopularEntity> entities = businessPopularRepository.findByBusinessIdInOrderByCountDesc(businessIds);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }
    
    @Override
    public List<BusinessPopularDto> getByBusinessIds(Iterable<UUID> businessIds, int size) {
        List<BusinessPopularDto> result = null;
        if (!IterableUtils.isEmpty(businessIds)) {
            PageRequest pageRequest = PageRequest.of(0, size, Sort.Direction.DESC, "count");
            List<BusinessPopularEntity> entities = businessPopularRepository.findByBusinessIdIn(businessIds, pageRequest);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }
}
