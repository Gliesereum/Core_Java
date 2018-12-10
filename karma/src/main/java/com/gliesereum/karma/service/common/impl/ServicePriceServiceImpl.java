package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.ServicePriceEntity;
import com.gliesereum.karma.model.repository.jpa.common.ServicePriceRepository;
import com.gliesereum.karma.service.common.ServicePriceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Slf4j
@Service
public class ServicePriceServiceImpl extends DefaultServiceImpl<ServicePriceDto, ServicePriceEntity> implements ServicePriceService {

    @Autowired
    private ServicePriceRepository repository;

    private static final Class<ServicePriceDto> DTO_CLASS = ServicePriceDto.class;
    private static final Class<ServicePriceEntity> ENTITY_CLASS = ServicePriceEntity.class;

    public ServicePriceServiceImpl(ServicePriceRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<ServicePriceDto> getAllByPackage(UUID id) {
        List<ServicePriceEntity> entities = repository.getByBusinessServiceId(id);
        return converter.convert(entities, dtoClass);
    }
}
