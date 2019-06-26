package com.gliesereum.karma.service.service.impl;

import com.gliesereum.karma.model.entity.service.ServiceClassEntity;
import com.gliesereum.karma.model.repository.jpa.service.ServiceClassRepository;
import com.gliesereum.karma.service.service.descriptions.ServiceClassDescriptionService;
import com.gliesereum.karma.service.service.ServiceClassService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.ServiceClassDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.service.ServiceClassDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Slf4j
@Service
public class ServiceClassServiceImpl extends DefaultServiceImpl<ServiceClassDto, ServiceClassEntity> implements ServiceClassService {

    private static final Class<ServiceClassDto> DTO_CLASS = ServiceClassDto.class;
    private static final Class<ServiceClassEntity> ENTITY_CLASS = ServiceClassEntity.class;

    private final ServiceClassRepository serviceClassRepository;

    @Autowired
    private ServiceClassDescriptionService serviceClassDescriptionService;

    @Autowired
    public ServiceClassServiceImpl(ServiceClassRepository serviceClassRepository, DefaultConverter defaultConverter) {
        super(serviceClassRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.serviceClassRepository = serviceClassRepository;
    }

    @Override
    public boolean existsService(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public ServiceClassDto create(ServiceClassDto dto) {
        ServiceClassDto result = null;
        if (dto != null) {
            result = super.create(dto);
            DescriptionReadableDto<ServiceClassDescriptionDto> descriptions = serviceClassDescriptionService.create(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
        }
        return result;
    }

    @Override
    public ServiceClassDto update(ServiceClassDto dto) {
        ServiceClassDto result = null;
        if (dto != null) {
            result = super.update(dto);
            DescriptionReadableDto<ServiceClassDescriptionDto> descriptions = serviceClassDescriptionService.update(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
        }
        return result;
    }
}
