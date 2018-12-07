package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.ServiceEntity;
import com.gliesereum.karma.model.repository.jpa.common.ServiceRepository;
import com.gliesereum.karma.service.common.ServiceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.ServiceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Slf4j
@Service
public class ServiceServiceImpl extends DefaultServiceImpl<ServiceDto, ServiceEntity> implements ServiceService {

    private static final Class<ServiceDto> DTO_CLASS = ServiceDto.class;
    private static final Class<ServiceEntity> ENTITY_CLASS = ServiceEntity.class;

    public ServiceServiceImpl(ServiceRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

}
