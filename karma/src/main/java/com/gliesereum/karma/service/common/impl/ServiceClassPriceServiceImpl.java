package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.aspect.annotation.UpdateCarWashIndex;
import com.gliesereum.karma.model.entity.common.ServiceClassPriceEntity;
import com.gliesereum.karma.model.repository.jpa.common.ServiceClassPriceRepository;
import com.gliesereum.karma.service.car.ServiceClassCarService;
import com.gliesereum.karma.service.common.ServiceClassPriceService;
import com.gliesereum.karma.service.common.ServicePriceService;
import com.gliesereum.karma.service.servicetype.ServiceTypeFacade;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.common.ServiceClassPriceDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.SERVICE_CLASS_NOT_FOUND;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.SERVICE_PRICE_NOT_FOUND;

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
    private ServiceTypeFacade serviceTypeFacade;

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    private ServiceClassCarService serviceClassCarService;

    @Autowired
    public ServiceClassPriceServiceImpl(ServiceClassPriceRepository serviceClassPriceRepository, DefaultConverter defaultConverter) {
        super(serviceClassPriceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.serviceClassPriceRepository = serviceClassPriceRepository;
    }

    @Override
    @UpdateCarWashIndex
    public ServiceClassPriceDto create(ServiceClassPriceDto dto) {
        if (dto != null)
            checkPermission(dto.getPriceId(), dto.getServiceClassId());
        return super.create(dto);
    }

    @Override
    @UpdateCarWashIndex
    public ServiceClassPriceDto update(ServiceClassPriceDto dto) {
        if (dto != null)
            checkPermission(dto.getPriceId(), dto.getServiceClassId());
        return super.update(dto);
    }

    @Override
    public void delete(UUID id) {
        if (id != null) {
            Optional<ServiceClassPriceEntity> entity = repository.findById(id);
            entity.ifPresent(i -> {
                checkPermission(i.getPriceId(), i.getServiceClassId());
                repository.delete(i);
            });

        }
    }

    @Override
    public void delete(UUID priceId, UUID classId) {
        if (ObjectUtils.allNotNull(priceId, classId)) {
            Optional<ServiceClassPriceEntity> entity = serviceClassPriceRepository.findByPriceIdAndServiceClassId(priceId, classId);
            entity.ifPresent(i -> {
                checkPermission(i.getPriceId(), i.getServiceClassId());
                repository.delete(i);
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
            if (price == null) {
                throw new ClientException(SERVICE_PRICE_NOT_FOUND);
            }
            serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(ServiceType.CAR_WASH, price.getCorporationServiceId());
            if (!serviceClassCarService.isExist(serviceClassId)) {
                throw new ClientException(SERVICE_CLASS_NOT_FOUND);
            }
        }
    }
}
