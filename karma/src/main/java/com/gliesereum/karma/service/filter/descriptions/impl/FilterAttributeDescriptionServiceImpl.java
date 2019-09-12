package com.gliesereum.karma.service.filter.descriptions.impl;

import com.gliesereum.karma.model.entity.filter.descriptions.FilterAttributeDescriptionEntity;
import com.gliesereum.karma.model.repository.jpa.filter.descriptions.FilterAttributeDescriptionRepository;
import com.gliesereum.karma.service.filter.descriptions.FilterAttributeDescriptionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.filter.descriptions.FilterAttributeDescriptionDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.service.descrption.impl.DefaultDescriptionServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class FilterAttributeDescriptionServiceImpl extends DefaultDescriptionServiceImpl<FilterAttributeDescriptionDto, FilterAttributeDescriptionEntity> implements FilterAttributeDescriptionService {

    private static final Class<FilterAttributeDescriptionDto> DTO_CLASS = FilterAttributeDescriptionDto.class;
    private static final Class<FilterAttributeDescriptionEntity> ENTITY_CLASS = FilterAttributeDescriptionEntity.class;

    private final FilterAttributeDescriptionRepository filterAttributeDescriptionRepository;

    @Autowired
    public FilterAttributeDescriptionServiceImpl(FilterAttributeDescriptionRepository filterAttributeDescriptionRepository, DefaultConverter defaultConverter) {
        super(filterAttributeDescriptionRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.filterAttributeDescriptionRepository = filterAttributeDescriptionRepository;
    }
}