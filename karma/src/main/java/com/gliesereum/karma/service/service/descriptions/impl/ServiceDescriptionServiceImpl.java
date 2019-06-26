package com.gliesereum.karma.service.service.descriptions.impl;

import com.gliesereum.karma.model.entity.service.descriptions.ServiceDescriptionEntity;
import com.gliesereum.karma.model.repository.jpa.service.descriptions.ServiceDescriptionRepository;
import com.gliesereum.karma.service.service.descriptions.ServiceDescriptionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.ServiceDescriptionDto;
import com.gliesereum.share.common.service.descrption.impl.DefaultDescriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class ServiceDescriptionServiceImpl extends DefaultDescriptionServiceImpl<ServiceDescriptionDto, ServiceDescriptionEntity> implements ServiceDescriptionService {

    private static final Class<ServiceDescriptionDto> DTO_CLASS = ServiceDescriptionDto.class;
    private static final Class<ServiceDescriptionEntity> ENTITY_CLASS = ServiceDescriptionEntity.class;

    private final ServiceDescriptionRepository serviceDescriptionRepository;

    @Autowired
    public ServiceDescriptionServiceImpl(ServiceDescriptionRepository serviceDescriptionRepository, DefaultConverter defaultConverter) {
        super(serviceDescriptionRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.serviceDescriptionRepository = serviceDescriptionRepository;
    }
}
