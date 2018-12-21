package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.aspect.annotation.UpdateCarWashIndex;
import com.gliesereum.karma.model.entity.common.ServicePriceEntity;
import com.gliesereum.karma.model.repository.jpa.common.ServicePriceRepository;
import com.gliesereum.karma.service.common.PackageService;
import com.gliesereum.karma.service.common.ServicePriceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.PackageDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    private final ServicePriceRepository servicePriceRepository;

    @Autowired
    private PackageService packageService;

    private static final Class<ServicePriceDto> DTO_CLASS = ServicePriceDto.class;
    private static final Class<ServicePriceEntity> ENTITY_CLASS = ServicePriceEntity.class;

    public ServicePriceServiceImpl(ServicePriceRepository servicePriceRepository, DefaultConverter defaultConverter) {
        super(servicePriceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.servicePriceRepository = servicePriceRepository;
    }

    @Override
    @UpdateCarWashIndex
    public ServicePriceDto create(ServicePriceDto dto) {
        return super.create(dto);
    }

    @Override
    @UpdateCarWashIndex
    public ServicePriceDto update(ServicePriceDto dto) {
        return super.update(dto);
    }

    @Override
    public List<ServicePriceDto> getAllByPackage(UUID id) {
        PackageDto packageDto = packageService.getById(id);
        if (packageDto != null && CollectionUtils.isNotEmpty(packageDto.getServices())) {
            return packageDto.getServices();
        }
        List<ServicePriceDto> emptyList = Collections.emptyList();
        return emptyList;
    }

    @Override
    public List<ServicePriceDto> getByUUIDs(List<UUID> ids) {
        List<ServicePriceEntity> entities = repository.findByIdIn(ids);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<ServicePriceDto> getByBusinessServiceId(UUID id) {
        List<ServicePriceEntity> entities = servicePriceRepository.findAllByBusinessServiceId(id);
        return converter.convert(entities, dtoClass);
    }
}
