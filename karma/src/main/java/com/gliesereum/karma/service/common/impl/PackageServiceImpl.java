package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.PackageEntity;
import com.gliesereum.karma.model.repository.jpa.common.PackageRepository;
import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.karma.service.common.PackageService;
import com.gliesereum.karma.service.common.PackageServiceService;
import com.gliesereum.karma.service.common.ServicePriceService;
import com.gliesereum.karma.service.servicetype.ServiceTypeFacade;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.model.dto.karma.common.PackageDto;
import com.gliesereum.share.common.model.dto.karma.common.PackageServiceDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.BODY_INVALID;
import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Slf4j
@Service
public class PackageServiceImpl extends DefaultServiceImpl<PackageDto, PackageEntity> implements PackageService {

    @Autowired
    private PackageRepository repository;

    @Autowired
    private PackageServiceService packageServiceService;

    @Autowired
    private ServiceTypeFacade serviceTypeFacade;

    @Autowired
    private CarWashService washService;

    @Autowired
    private ServicePriceService servicePriceService;

    private static final Class<PackageDto> DTO_CLASS = PackageDto.class;
    private static final Class<PackageEntity> ENTITY_CLASS = PackageEntity.class;

    public PackageServiceImpl(PackageRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<PackageDto> getByCorporationServiceId(UUID id) {
        List<PackageEntity> entities = repository.getByCorporationServiceId(id);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public PackageDto create(PackageDto dto) {
        checkPermission(dto);
        checkServicesInCarWash(dto);
        PackageDto result = super.create(dto);
        setServices(dto, result);
        return result;

    }


    @Override
    @Transactional
    public PackageDto update(PackageDto dto) {
        checkPermission(dto);
        checkServicesInCarWash(dto);
        PackageDto result = super.update(dto);
        deletePackageServicePrice(dto);
        setServices(dto, result);
        return result;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id != null) {
            PackageDto dto = getById(id);
            if (dto != null) {
                deletePackageServicePrice(dto);
            }
            super.delete(id);
        }
    }

    private void deletePackageServicePrice(PackageDto dto) {
        List<UUID> servicesIds = dto.getServices().stream().map(ServicePriceDto::getId).collect(Collectors.toList());
        packageServiceService.deleteByPackageIdAndServicePriceIDs(dto.getId(), servicesIds);
    }

    private void setServices(PackageDto dto, PackageDto result) {
        if (result != null) {
            result.setServicesIds(dto.getServicesIds());
            dto.getServicesIds().forEach(f -> {
                packageServiceService.create(new PackageServiceDto(result.getId(), f));
            });
        }
    }

    private void checkServicesInCarWash(PackageDto dto) {
        if (dto.getServicesIds().isEmpty()) {
            throw new ClientException(SERVICE_NOT_CHOOSE);
        }
        CarWashDto carWash = washService.getById(dto.getCorporationServiceId());
        if (carWash == null) {
            throw new ClientException(CARWASH_NOT_FOUND);
        }
        List<ServicePriceDto> services = servicePriceService.getByIds(dto.getServicesIds());
        if (dto.getServicesIds().size() != services.size()) {
            throw new ClientException(SERVICE_NOT_FOUND);
        }
        if (CollectionUtils.isNotEmpty(services)) {
            services.forEach(f -> {
                if (!f.getCorporationServiceId().equals(dto.getCorporationServiceId())) {
                    throw new ClientException(SERVICE_PRICE_NOT_FOUND_IN_CARWASH);
                }
            });
        } else throw new ClientException(SERVICE_NOT_FOUND);
    }

    private void checkPermission(PackageDto dto) {
        if (dto == null) {
            throw new ClientException(BODY_INVALID);
        }
        if (dto.getCorporationServiceId() == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(ServiceType.CAR_WASH, dto.getCorporationServiceId());
    }
}
