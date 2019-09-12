package com.gliesereum.karma.service.filter.descriptions.impl;

import com.gliesereum.karma.model.entity.filter.descriptions.FilterDescriptionEntity;
import com.gliesereum.karma.model.repository.jpa.filter.descriptions.FilterDescriptionRepository;
import com.gliesereum.karma.service.filter.descriptions.FilterDescriptionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.filter.descriptions.FilterDescriptionDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.service.descrption.impl.DefaultDescriptionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FilterDescriptionServiceImpl extends DefaultDescriptionServiceImpl<FilterDescriptionDto, FilterDescriptionEntity> implements FilterDescriptionService {

    private static final Class<FilterDescriptionDto> DTO_CLASS = FilterDescriptionDto.class;
    private static final Class<FilterDescriptionEntity> ENTITY_CLASS = FilterDescriptionEntity.class;

    private final FilterDescriptionRepository filterDescriptionRepository;

    @Autowired
    public FilterDescriptionServiceImpl(FilterDescriptionRepository filterDescriptionRepository, DefaultConverter defaultConverter) {
        super(filterDescriptionRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.filterDescriptionRepository = filterDescriptionRepository;
    }
}