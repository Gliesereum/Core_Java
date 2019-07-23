package com.gliesereum.karma.service.service.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.model.entity.service.PackageEntity;
import com.gliesereum.karma.model.repository.jpa.service.PackageRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.service.PackageService;
import com.gliesereum.karma.service.service.PackageServiceService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.karma.service.service.descriptions.PackageDescriptionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.service.LitePackageDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageServiceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.PackageDescriptionDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.BODY_INVALID;
import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class PackageServiceImpl extends DefaultServiceImpl<PackageDto, PackageEntity> implements PackageService {

    private static final Class<PackageDto> DTO_CLASS = PackageDto.class;
    private static final Class<PackageEntity> ENTITY_CLASS = PackageEntity.class;

    private final PackageRepository packageRepository;

    @Autowired
    private PackageServiceService packageServiceService;

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    private PackageDescriptionService packageDescriptionService;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository, DefaultConverter defaultConverter) {
        super(packageRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.packageRepository = packageRepository;
    }

    @Override
    public List<PackageDto> getByBusinessId(UUID id) {
        List<PackageEntity> entities = packageRepository.getByBusinessIdAndObjectState(id, ObjectState.ACTIVE);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public PackageDto getByIdIgnoreState(UUID id) {
        PackageDto result = null;
        if (id != null) {
            Optional<PackageEntity> entity = repository.findById(id);
            if (entity.isPresent()) {
                result = converter.convert(entity.get(), dtoClass);
            }
        }
        return result;
    }

    @Override
    public List<LitePackageDto> getLitePackageByBusinessId(UUID id) {
        List<PackageEntity> entities = packageRepository.getByBusinessIdAndObjectState(id, ObjectState.ACTIVE);
        return converter.convert(entities, LitePackageDto.class);
    }

    @Override
    public List<PackageDto> getAll() {
        List<PackageEntity> entities = packageRepository.getAllByObjectState(ObjectState.ACTIVE);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public PackageDto getById(UUID id) {
        PackageEntity entity = packageRepository.findByIdAndObjectState(id, ObjectState.ACTIVE);
        return converter.convert(entity, dtoClass);
    }

    @Override
    public LitePackageDto getLiteById(UUID id) {
        PackageEntity entity = packageRepository.findByIdAndObjectState(id, ObjectState.ACTIVE);
        return converter.convert(entity, LitePackageDto.class);
    }

    @Override
    public List<PackageDto> getByIds(Iterable<UUID> ids) {
        List<PackageDto> result = null;
        if (ids != null) {
            List<PackageEntity> entities = packageRepository.getAllByIdInAndObjectState(ids, ObjectState.ACTIVE);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    @Transactional
    public PackageDto create(PackageDto dto) {
        checkPermission(dto);
        checkServicesInBusiness(dto);
        dto.setObjectState(ObjectState.ACTIVE);
        PackageDto result = super.create(dto);
        setServices(dto, result);
        List<PackageDescriptionDto> descriptions = packageDescriptionService.create(dto.getDescriptions(), result.getId());
        result.setDescriptions(descriptions);
        return result;

    }

    @Override
    @Transactional
    public PackageDto update(PackageDto dto) {
        checkPermission(dto);
        checkServicesInBusiness(dto);
        PackageDto oldDto = getById(dto.getId());
        if (oldDto == null) {
            throw new ClientException(PACKAGE_NOT_FOUND);
        }
        dto.setObjectState(oldDto.getObjectState());
        PackageDto result = super.update(dto);
        deletePackageServicePrice(dto);
        setServices(dto, result);
        List<PackageDescriptionDto> descriptions = packageDescriptionService.update(dto.getDescriptions(), result.getId());
        result.setDescriptions(descriptions);
        return result;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        PackageDto dto = getById(id);
        if (dto == null) {
            throw new ClientException(SERVICE_NOT_FOUND);
        }
        dto.setObjectState(ObjectState.DELETED);
        super.update(dto);
    }

    private void deletePackageServicePrice(PackageDto dto) {
        packageServiceService.deleteByPackageId(dto.getId());
    }

    private void setServices(PackageDto dto, PackageDto result) {
        if (result != null) {
            result.setServicesIds(dto.getServicesIds());
            if (CollectionUtils.isNotEmpty(result.getServicesIds())) {
                List<ServicePriceDto> services = servicePriceService.getByIds(result.getServicesIds());
                result.setServices(services);
            }
            dto.getServicesIds().forEach(f -> packageServiceService.create(new PackageServiceDto(result.getId(), f)));
        }
    }

    private void checkServicesInBusiness(PackageDto dto) {
        if (dto.getServicesIds().isEmpty()) {
            throw new ClientException(SERVICE_NOT_CHOOSE);
        }
        BaseBusinessDto business = baseBusinessService.getById(dto.getBusinessId());
        if (business == null) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        List<ServicePriceDto> services = servicePriceService.getByIds(dto.getServicesIds());
        if (dto.getServicesIds().size() != services.size()) {
            throw new ClientException(SERVICE_NOT_FOUND);
        }
        if (CollectionUtils.isNotEmpty(services)) {
            services.forEach(f -> {
                if (!f.getBusinessId().equals(dto.getBusinessId())) {
                    throw new ClientException(SERVICE_PRICE_NOT_FOUND_IN_BUSINESS);
                }
            });
        } else throw new ClientException(SERVICE_NOT_FOUND);
    }

    private void checkPermission(PackageDto dto) {
        if (dto == null) {
            throw new ClientException(BODY_INVALID);
        }
        if (dto.getBusinessId() == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }

        BaseBusinessDto business = baseBusinessService.getById(dto.getBusinessId());
        if (business == null) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        businessPermissionFacade.checkCurrentUserIsOwnerBusiness(dto.getBusinessId());
    }
}
