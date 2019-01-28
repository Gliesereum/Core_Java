package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.aspect.annotation.UpdateCarWashIndex;
import com.gliesereum.karma.model.entity.common.ServicePriceEntity;
import com.gliesereum.karma.model.repository.jpa.common.ServicePriceRepository;
import com.gliesereum.karma.service.common.*;
import com.gliesereum.karma.service.servicetype.ServiceTypeFacade;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.common.*;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

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

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private ServiceTypeFacade serviceTypeFacade;

    @Autowired
    private PriceFilterAttributeService priceFilterAttributeService;

    @Autowired
    private FilterAttributeService filterAttributeService;

    @Autowired
    private FilterService filterService;

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
    public List<ServicePriceDto> getByBusinessId(UUID id) {
        List<ServicePriceEntity> entities = servicePriceRepository.findAllByBusinessId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public ServicePriceDto addFilterAttribute(UUID idPrice, UUID idAttribute) {
        ServicePriceDto price = getPrice(idPrice);
        checkFilterAttribute(idAttribute, price.getService().getServiceType());
        priceFilterAttributeService.create(new PriceFilterAttributeDto(idPrice, idAttribute));
        return getById(idPrice);
    }

    @Override
    @Transactional
    public ServicePriceDto removeFilterAttribute(UUID idPrice, UUID idAttribute) {
        getPrice(idPrice);
        checkFilterAttributeExist(idPrice, idAttribute);
        priceFilterAttributeService.deleteByPriceIdAndFilterId(idPrice, idAttribute);
        return getById(idPrice);
    }

    private void checkFilterAttributeExist(UUID idPrice, UUID idAttribute) {
        if (!priceFilterAttributeService.existByPriceIdAndAttributeId(idPrice, idAttribute)) {
            throw new ClientException(FILTER_ATTRIBUTE_NOT_FOUND_WITH_PRICE);
        }
    }

    private void checkPermission(ServicePriceDto dto) {
        if (dto.getBusinessId() == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        ServiceDto serviceDto = getServiceById(dto.getServiceId());
        serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(serviceDto.getServiceType(), dto.getBusinessId());
    }

    private ServiceDto getServiceById(UUID serviceId) {
        ServiceDto result = null;
        if (serviceId == null) {
            throw new ClientException(SERVICE_ID_IS_EMPTY);
        }
        result = serviceService.getById(serviceId);
        if (result == null) {
            throw new ClientException(SERVICE_NOT_FOUND);
        }
        return result;
    }

    private ServicePriceDto getPrice(UUID idPrice) {
        ServicePriceDto dto = getById(idPrice);
        if (dto == null) {
            throw new ClientException(SERVICE_PRICE_NOT_FOUND);
        }
        checkPermission(dto);
        return dto;
    }

    private void checkFilterAttribute(UUID idAttribute, ServiceType serviceType) {
        FilterAttributeDto attribute = filterAttributeService.getById(idAttribute);
        if (attribute == null) {
            throw new ClientException(FILTER_ATTRIBUTE_NOT_FOUND);
        }
        FilterDto filter = filterService.getById(attribute.getFilterId());
        if (!filter.getServiceType().equals(serviceType)) {
            throw new ClientException(FILTER_ATTRIBUTE_NOT_FOUND_BY_SERVICE_TYPE);
        }
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
