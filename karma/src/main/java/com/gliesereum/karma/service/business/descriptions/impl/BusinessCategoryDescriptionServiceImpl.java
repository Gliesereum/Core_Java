package com.gliesereum.karma.service.business.descriptions.impl;

import com.gliesereum.karma.model.entity.business.descriptions.BusinessCategoryDescriptionEntity;
import com.gliesereum.karma.model.repository.jpa.business.descriptions.BusinessCategoryDescriptionRepository;
import com.gliesereum.karma.service.business.descriptions.BusinessCategoryDescriptionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.business.descriptions.BusinessCategoryDescriptionDto;
import com.gliesereum.share.common.service.descrption.impl.DefaultDescriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class BusinessCategoryDescriptionServiceImpl extends DefaultDescriptionServiceImpl<BusinessCategoryDescriptionDto, BusinessCategoryDescriptionEntity> implements BusinessCategoryDescriptionService {

    private static final Class<BusinessCategoryDescriptionDto> DTO_CLASS = BusinessCategoryDescriptionDto.class;
    private static final Class<BusinessCategoryDescriptionEntity> ENTITY_CLASS = BusinessCategoryDescriptionEntity.class;

    private final BusinessCategoryDescriptionRepository businessCategoryDescriptionRepository;

    @Autowired
    public BusinessCategoryDescriptionServiceImpl(BusinessCategoryDescriptionRepository businessCategoryDescriptionRepository, DefaultConverter defaultConverter) {
        super(businessCategoryDescriptionRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.businessCategoryDescriptionRepository = businessCategoryDescriptionRepository;
    }
}
