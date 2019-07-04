package com.gliesereum.karma.service.service.impl;

import com.gliesereum.karma.model.entity.service.ServiceEntity;
import com.gliesereum.karma.model.repository.jpa.service.ServiceRepository;
import com.gliesereum.karma.service.service.ServiceService;
import com.gliesereum.karma.service.service.descriptions.ServiceDescriptionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.service.ServiceDto;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.ServiceDescriptionDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.SERVICE_NOT_FOUND;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class ServiceServiceImpl extends DefaultServiceImpl<ServiceDto, ServiceEntity> implements ServiceService {

    private static final Class<ServiceDto> DTO_CLASS = ServiceDto.class;
    private static final Class<ServiceEntity> ENTITY_CLASS = ServiceEntity.class;

    private final ServiceRepository serviceRepository;

    @Autowired
    private ServiceDescriptionService serviceDescriptionService;

    @Autowired
    public ServiceServiceImpl(ServiceRepository serviceRepository, DefaultConverter defaultConverter) {
        super(serviceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.serviceRepository = serviceRepository;
    }

    @Override
    public ServiceDto create(ServiceDto dto) {
        ServiceDto result = null;
        if (dto != null) {
            dto.setObjectState(ObjectState.ACTIVE);
            result = super.create(dto);
            List<ServiceDescriptionDto> descriptions = serviceDescriptionService.create(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
        }
        return result;
    }

    @Override
    @Transactional
    public ServiceDto update(ServiceDto dto) {
        ServiceDto result = null;
        if (dto != null) {
            ServiceDto oldDto = getById(dto.getId());
            if (oldDto == null) {
                throw new ClientException(SERVICE_NOT_FOUND);
            }
            dto.setObjectState(oldDto.getObjectState());
            result = super.update(dto);
            List<ServiceDescriptionDto> descriptions = serviceDescriptionService.update(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
        }
        return result;
    }

    @Override
    public List<ServiceDto> getAll() {
        List<ServiceEntity> entities = serviceRepository.getAllByObjectState(ObjectState.ACTIVE);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public ServiceDto getById(UUID id) {
        ServiceEntity entity = serviceRepository.findByIdAndObjectState(id, ObjectState.ACTIVE);
        return converter.convert(entity, dtoClass);
    }

    @Override
    public List<ServiceDto> getByIds(Iterable<UUID> ids) {
        List<ServiceDto> result = null;
        if (ids != null) {
            List<ServiceEntity> entities = serviceRepository.getAllByIdInAndObjectState(ids, ObjectState.ACTIVE);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        ServiceDto dto = getById(id);
        if (dto == null) {
            throw new ClientException(SERVICE_NOT_FOUND);
        }
        dto.setObjectState(ObjectState.DELETED);
        super.update(dto);
    }

    @Override
    public List<ServiceDto> getAllByBusinessCategoryId(UUID businessCategoryId) {
        List<ServiceDto> result = null;
        if (businessCategoryId != null) {
            List<ServiceEntity> entities = serviceRepository.getAllByBusinessCategoryIdAndObjectStateOrderByName(businessCategoryId, ObjectState.ACTIVE);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }
}
