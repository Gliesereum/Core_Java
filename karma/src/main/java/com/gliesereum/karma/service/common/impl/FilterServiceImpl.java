package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.FilterEntity;
import com.gliesereum.karma.model.repository.jpa.common.FilterRepository;
import com.gliesereum.karma.service.common.FilterService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.FilterDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<FilterDto> getAllByServiceType(ServiceType serviceType) {
        List<FilterEntity> entities = repository.findAllByServiceType(serviceType);
        return converter.convert(entities, dtoClass);
    }
}
