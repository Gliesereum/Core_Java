package com.gliesereum.karma.service.service.impl;

import com.gliesereum.karma.model.entity.service.ServiceClassEntity;
import com.gliesereum.karma.model.repository.jpa.service.ServiceClassRepository;
import com.gliesereum.karma.service.service.ServiceClassService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.service.ServiceClassDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Slf4j
@Service
public class ServiceClassServiceImpl extends DefaultServiceImpl<ServiceClassDto, ServiceClassEntity> implements ServiceClassService {

    @Autowired
    private ServiceClassRepository repository;

    private static final Class<ServiceClassDto> DTO_CLASS = ServiceClassDto.class;
    private static final Class<ServiceClassEntity> ENTITY_CLASS = ServiceClassEntity.class;

    public ServiceClassServiceImpl(ServiceClassRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public boolean existsService(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public List<ServiceClassDto> getAllByServiceType(ServiceType type) {
        List<ServiceClassDto> result = null;
        if (type != null) {
            List<ServiceClassEntity> entities = repository.findAllByServiceType(type);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }
}
