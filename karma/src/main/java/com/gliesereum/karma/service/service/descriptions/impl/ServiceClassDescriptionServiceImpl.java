package com.gliesereum.karma.service.service.descriptions.impl;

import com.gliesereum.karma.model.entity.service.descriptions.ServiceClassDescriptionEntity;
import com.gliesereum.karma.model.repository.jpa.service.descriptions.ServiceClassDescriptionRepository;
import com.gliesereum.karma.service.service.descriptions.ServiceClassDescriptionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.ServiceClassDescriptionDto;
import com.gliesereum.share.common.service.descrption.impl.DefaultDescriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class ServiceClassDescriptionServiceImpl extends DefaultDescriptionServiceImpl<ServiceClassDescriptionDto, ServiceClassDescriptionEntity> implements ServiceClassDescriptionService {

    private static final Class<ServiceClassDescriptionDto> DTO_CLASS = ServiceClassDescriptionDto.class;
    private static final Class<ServiceClassDescriptionEntity> ENTITY_CLASS = ServiceClassDescriptionEntity.class;

    private final ServiceClassDescriptionRepository serviceClassDescriptionRepository;

    @Autowired
    public ServiceClassDescriptionServiceImpl(ServiceClassDescriptionRepository serviceClassDescriptionRepository, DefaultConverter defaultConverter) {
        super(serviceClassDescriptionRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.serviceClassDescriptionRepository = serviceClassDescriptionRepository;
    }
}
