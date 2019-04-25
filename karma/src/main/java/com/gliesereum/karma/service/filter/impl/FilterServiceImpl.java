package com.gliesereum.karma.service.filter.impl;

import com.gliesereum.karma.model.entity.filter.FilterEntity;
import com.gliesereum.karma.model.repository.jpa.filter.FilterRepository;
import com.gliesereum.karma.service.filter.FilterService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.filter.FilterDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class FilterServiceImpl extends DefaultServiceImpl<FilterDto, FilterEntity> implements FilterService {

    @Autowired
    private FilterRepository repository;

    private static final Class<FilterDto> DTO_CLASS = FilterDto.class;
    private static final Class<FilterEntity> ENTITY_CLASS = FilterEntity.class;

    public FilterServiceImpl(FilterRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<FilterDto> getAllByBusinessCategoryId(UUID businessCategoryId) {
        List<FilterEntity> entities = repository.findAllByBusinessCategoryId(businessCategoryId);
        return converter.convert(entities, dtoClass);
    }
}
