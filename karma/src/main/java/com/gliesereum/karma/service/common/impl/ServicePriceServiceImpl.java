package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.aspect.annotation.UpdateCarWashIndex;
import com.gliesereum.karma.model.entity.common.ServicePriceEntity;
import com.gliesereum.karma.model.repository.jpa.common.ServicePriceRepository;
import com.gliesereum.karma.service.common.PackageService;
import com.gliesereum.karma.service.common.ServicePriceService;
import com.gliesereum.karma.service.common.ServiceService;
import com.gliesereum.karma.service.servicetype.ServiceTypeFacade;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.common.PackageDto;
import com.gliesereum.share.common.model.dto.karma.common.ServiceDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
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
public class ServicePriceServiceImpl extends DefaultServiceImpl<ServicePriceDto, ServicePriceEntity> implements ServicePriceService {

    private final ServicePriceRepository servicePriceRepository;

    @Autowired
    private PackageService packageService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceTypeFacade serviceTypeFacade;

    private static final Class<ServicePriceDto> DTO_CLASS = ServicePriceDto.class;
    private static final Class<ServicePriceEntity> ENTITY_CLASS = ServicePriceEntity.class;

    public ServicePriceServiceImpl(ServicePriceRepository servicePriceRepository, DefaultConverter defaultConverter) {
        super(servicePriceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.servicePriceRepository = servicePriceRepository;
    }

    @Override
    @UpdateCarWashIndex
    public ServicePriceDto create(ServicePriceDto dto) {
        checkPermission(dto);
        return super.create(setCustomName(dto));
    }

    @Override
    @UpdateCarWashIndex
    public ServicePriceDto update(ServicePriceDto dto) {
        checkPermission(dto);
        return super.update(setCustomName(dto));
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
    public List<ServicePriceDto> getByCorporationServiceId(UUID id) {
        List<ServicePriceEntity> entities = servicePriceRepository.findAllByCorporationServiceId(id);
        return converter.convert(entities, dtoClass);
    }

    private void checkPermission(ServicePriceDto dto) {
        if(dto.getCorporationServiceId() == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(ServiceType.CAR_WASH, dto.getCorporationServiceId());
    }

    private ServicePriceDto setCustomName(ServicePriceDto dto) {
        if (StringUtils.isEmpty(dto.getName())) {
            ServiceDto service = serviceService.getById(dto.getServiceId());
            if (service == null) {
                throw new ClientException(SERVICE_NOT_FOUND);
            }
            dto.setName(service.getName());
        }
        return dto;
    }
}
