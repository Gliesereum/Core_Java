package com.gliesereum.karma.service.service.impl;

import com.gliesereum.karma.model.entity.service.ServiceEntity;
import com.gliesereum.karma.model.repository.jpa.service.ServiceRepository;
import com.gliesereum.karma.service.service.ServiceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.service.ServiceDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
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

    private final ServiceRepository repository;

    private static final Class<ServiceDto> DTO_CLASS = ServiceDto.class;
    private static final Class<ServiceEntity> ENTITY_CLASS = ServiceEntity.class;

    public ServiceServiceImpl(ServiceRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.repository = repository;
    }

    @Override
    public ServiceDto create(ServiceDto dto) {
        ServiceDto result = null;
        if (dto != null) {
            dto.setObjectState(ObjectState.ACTIVE);
            result = super.create(dto);
        }
        return result;
    }

    @Override
    @Transactional
    public ServiceDto update(ServiceDto dto) {
        ServiceDto oldDto = getById(dto.getId());
        if (oldDto == null) {
            throw new ClientException(SERVICE_NOT_FOUND);
        }
        dto.setObjectState(oldDto.getObjectState());
        return super.update(dto);
    }

    @Override
    public List<ServiceDto> getAll() {
        List<ServiceEntity> entities = repository.getAllByObjectState(ObjectState.ACTIVE);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public ServiceDto getById(UUID id) {
        ServiceEntity entity = repository.findByIdAndObjectState(id, ObjectState.ACTIVE);
        return converter.convert(entity, dtoClass);
    }

    @Override
    public List<ServiceDto> getByIds(Iterable<UUID> ids) {
        List<ServiceDto> result = null;
        if (ids != null) {
            List<ServiceEntity> entities = repository.getAllByIdInAndObjectState(ids, ObjectState.ACTIVE);
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
}
