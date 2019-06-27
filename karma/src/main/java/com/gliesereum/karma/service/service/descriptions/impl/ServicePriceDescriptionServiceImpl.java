package com.gliesereum.karma.service.service.descriptions.impl;

import com.gliesereum.karma.model.entity.service.descriptions.ServicePriceDescriptionEntity;
import com.gliesereum.karma.model.repository.jpa.service.descriptions.ServicePriceDescriptionRepository;
import com.gliesereum.karma.service.service.descriptions.ServicePriceDescriptionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.ServicePriceDescriptionDto;
import com.gliesereum.share.common.service.descrption.impl.DefaultDescriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class ServicePriceDescriptionServiceImpl extends DefaultDescriptionServiceImpl<ServicePriceDescriptionDto, ServicePriceDescriptionEntity> implements ServicePriceDescriptionService {

    private static final Class<ServicePriceDescriptionDto> DTO_CLASS = ServicePriceDescriptionDto.class;
    private static final Class<ServicePriceDescriptionEntity> ENTITY_CLASS = ServicePriceDescriptionEntity.class;

    private final ServicePriceDescriptionRepository servicePriceDescriptionRepository;

    @Autowired
    public ServicePriceDescriptionServiceImpl(ServicePriceDescriptionRepository servicePriceDescriptionRepository, DefaultConverter defaultConverter) {
        super(servicePriceDescriptionRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.servicePriceDescriptionRepository = servicePriceDescriptionRepository;
    }
}
