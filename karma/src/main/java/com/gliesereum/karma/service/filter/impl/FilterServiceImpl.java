package com.gliesereum.karma.service.filter.impl;

import com.gliesereum.karma.model.entity.filter.FilterEntity;
import com.gliesereum.karma.model.repository.jpa.filter.FilterRepository;
import com.gliesereum.karma.service.business.BusinessCategoryService;
import com.gliesereum.karma.service.filter.FilterService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.business.BusinessCategoryDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.BusinessType;
import com.gliesereum.share.common.model.dto.karma.filter.FilterDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class FilterServiceImpl extends DefaultServiceImpl<FilterDto, FilterEntity> implements FilterService {

    private static final Class<FilterDto> DTO_CLASS = FilterDto.class;
    private static final Class<FilterEntity> ENTITY_CLASS = FilterEntity.class;

    private final FilterRepository filterRepository;

    @Autowired
    private BusinessCategoryService businessCategoryService;

    @Autowired
    public FilterServiceImpl(FilterRepository filterRepository, DefaultConverter defaultConverter) {
        super(filterRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.filterRepository = filterRepository;
    }

    @Override
    public List<FilterDto> getAllByBusinessCategoryId(UUID businessCategoryId) {
        List<FilterEntity> entities = filterRepository.findAllByBusinessCategoryId(businessCategoryId);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<FilterDto> getAllByBusinessType(BusinessType businessType) {
        List<FilterDto> result = null;
        if (businessType != null) {
            List<BusinessCategoryDto> businessCategories = businessCategoryService.getByBusinessType(businessType);
            if (CollectionUtils.isNotEmpty(businessCategories)) {
                List<UUID> ids = businessCategories.stream()
                        .map(BusinessCategoryDto::getId)
                        .collect(Collectors.toList());
                List<FilterEntity> entities = filterRepository.findAllByBusinessCategoryIdIn(ids);
                result = converter.convert(entities, dtoClass);
            }
        }
        return result;
    }
}
