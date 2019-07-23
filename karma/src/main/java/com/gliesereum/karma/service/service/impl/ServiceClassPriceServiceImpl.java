package com.gliesereum.karma.service.service.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.model.entity.service.ServiceClassPriceEntity;
import com.gliesereum.karma.model.repository.jpa.service.ServiceClassPriceRepository;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.service.ServiceClassPriceService;
import com.gliesereum.karma.service.service.ServiceClassService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.service.ServiceClassPriceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class ServiceClassPriceServiceImpl extends DefaultServiceImpl<ServiceClassPriceDto, ServiceClassPriceEntity> implements ServiceClassPriceService {

    private static final Class<ServiceClassPriceDto> DTO_CLASS = ServiceClassPriceDto.class;
    private static final Class<ServiceClassPriceEntity> ENTITY_CLASS = ServiceClassPriceEntity.class;

    private final ServiceClassPriceRepository serviceClassPriceRepository;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    private ServiceClassService serviceClassService;

    @Autowired
    private BusinessEsService businessEsService;

    @Autowired
    public ServiceClassPriceServiceImpl(ServiceClassPriceRepository serviceClassPriceRepository, DefaultConverter defaultConverter) {
        super(serviceClassPriceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.serviceClassPriceRepository = serviceClassPriceRepository;
    }

    @Override
    public ServiceClassPriceDto create(ServiceClassPriceDto dto) {
        ServiceClassPriceDto result = null;
        if (dto != null) {
            checkPriceServiceExist(dto.getPriceId(), dto.getServiceClassId());
            ServicePriceDto price = servicePriceService.getById(dto.getPriceId());
            checkPermission(price, dto.getServiceClassId());
            result = super.create(dto);
        }
        return result;
    }

    @Override
    public ServiceClassPriceDto update(ServiceClassPriceDto dto) {
        ServiceClassPriceDto result = null;
        if (dto != null) {
            checkPriceServiceExist(dto.getPriceId(), dto.getServiceClassId());
            ServicePriceDto price = servicePriceService.getById(dto.getPriceId());
            checkPermission(price, dto.getServiceClassId());
            result = super.update(dto);
        }
        return result;
    }

    @Override
    public ServicePriceDto createAndGetPrice(ServiceClassPriceDto dto) {
        ServicePriceDto servicePrice = null;
        dto = this.create(dto);
        if (dto != null) {
            servicePrice = servicePriceService.getByIdAndRefresh(dto.getPriceId());
            businessEsService.indexAsync(servicePrice.getBusinessId());
        }
        return servicePrice;
    }

    @Override
    public ServicePriceDto updateAndGetPrice(ServiceClassPriceDto dto) {
        ServicePriceDto servicePrice = null;
        dto = this.update(dto);
        if (dto != null) {
            servicePrice = servicePriceService.getByIdAndRefresh(dto.getPriceId());
            businessEsService.indexAsync(servicePrice.getBusinessId());
        }
        return servicePrice;
    }

    @Override
    @Transactional
    public ServicePriceDto createAndGetPrice(UUID idPrice, List<UUID> classes) {
        ServicePriceDto result;
        if (idPrice == null) {
            throw new ClientException(PRICE_ID_IS_EMPTY);
        }
        ServicePriceDto price = servicePriceService.getById(idPrice);
        checkPermission(price);
        if (CollectionUtils.isNotEmpty(classes)) {
            classes.forEach(i -> {
                if (!serviceClassService.isExist(i)) {
                    throw new ClientException(SERVICE_CLASS_NOT_FOUND);
                }
            });
        }
        serviceClassPriceRepository.deleteByPriceId(idPrice);
        if (CollectionUtils.isNotEmpty(classes)) {
            List<ServiceClassPriceDto> classPrice = classes.stream().map(i -> new ServiceClassPriceDto(idPrice, i)).collect(Collectors.toList());
            this.create(classPrice);
        }
        result = servicePriceService.getByIdAndRefresh(idPrice);
        businessEsService.indexAsync(price.getBusinessId());
        return result;
    }

    @Override
    public void delete(UUID id) {
        if (id != null) {
            Optional<ServiceClassPriceEntity> entity = repository.findById(id);
            entity.ifPresent(i -> {
                ServicePriceDto price = servicePriceService.getById(i.getPriceId());
                checkPermission(price, i.getServiceClassId());
                repository.delete(i);
                businessEsService.indexAsync(price.getBusinessId());
            });

        }
    }

    @Override
    public void delete(UUID priceId, UUID classId) {
        if (ObjectUtils.allNotNull(priceId, classId)) {
            Optional<ServiceClassPriceEntity> entity = serviceClassPriceRepository.findByPriceIdAndServiceClassId(priceId, classId);
            entity.ifPresent(i -> {
                ServicePriceDto price = servicePriceService.getById(i.getPriceId());
                checkPermission(price, i.getServiceClassId());
                repository.delete(i);
                businessEsService.indexAsync(price.getBusinessId());
            });

        }
    }

    @Override
    public List<ServiceClassPriceDto> getByPriceId(UUID priceId) {
        List<ServiceClassPriceDto> result = null;
        if (priceId != null) {
            List<ServiceClassPriceEntity> allByPriceId = serviceClassPriceRepository.findAllByPriceId(priceId);
            result = converter.convert(allByPriceId, dtoClass);
        }
        return result;
    }

    private void checkPermission(UUID servicePriceId, UUID serviceClassId) {
        if (ObjectUtils.allNotNull(servicePriceId, serviceClassId)) {
            ServicePriceDto price = servicePriceService.getById(servicePriceId);
            checkPermission(price, serviceClassId);
        }
    }

    private void checkPermission(ServicePriceDto price, UUID serviceClassId) {
        if (serviceClassId != null) {
            checkPermission(price);
            if (!serviceClassService.isExist(serviceClassId)) {
                throw new ClientException(SERVICE_CLASS_NOT_FOUND);
            }
        }
    }

    private void checkPermission(ServicePriceDto price) {
        if (price == null) {
            throw new ClientException(SERVICE_PRICE_NOT_FOUND);
        }
        businessPermissionFacade.checkCurrentUserIsOwnerBusiness(price.getBusinessId());
    }

    private void checkPriceServiceExist(UUID priceId, UUID serviceClassId) {
        if (priceId == null) {
            throw new ClientException(PRICE_ID_IS_EMPTY);
        }
        if (serviceClassId == null) {
            throw new ClientException(SERVICE_CLASS_ID_IS_EMPTY);
        }
        if (serviceClassPriceRepository.existsByPriceIdAndServiceClassId(priceId, serviceClassId)) {
            throw new ClientException(PAR_SERVICE_CLASS_ID_AND_PRICE_ID_EXIST);
        }
    }
}
