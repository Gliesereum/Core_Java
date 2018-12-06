package com.gliesereum.karma.service.car.impl;

import com.gliesereum.karma.model.entity.car.ServiceClassCarEntity;
import com.gliesereum.karma.model.repository.jpa.car.ServiceClassCarRepository;
import com.gliesereum.karma.service.car.ServiceClassCarService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.car.ServiceClassCarDto;
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
public class ServiceClassCarServiceImpl extends DefaultServiceImpl<ServiceClassCarDto, ServiceClassCarEntity> implements ServiceClassCarService {

    @Autowired
    private ServiceClassCarRepository repository;

    private static final Class<ServiceClassCarDto> DTO_CLASS = ServiceClassCarDto.class;
    private static final Class<ServiceClassCarEntity> ENTITY_CLASS = ServiceClassCarEntity.class;

    public ServiceClassCarServiceImpl(ServiceClassCarRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public boolean existsService(UUID id) {
        return repository.existsById(id);
    }
}
