package com.gliesereum.karma.service.service.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.model.entity.service.PackageEntity;
import com.gliesereum.karma.model.repository.jpa.service.PackageRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.service.PackageService;
import com.gliesereum.karma.service.service.PackageServiceService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.karma.service.service.descriptions.PackageDescriptionService;
import com.gliesereum.karma.service.tag.ObjectTagService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.service.LitePackageDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageServiceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.PackageDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private BusinessEsService businessEsService;

    @Autowired
    private ObjectTagService objectTagService;

    @Autowired
    public PackageServiceImpl(PackageRepository packageRepository, DefaultConverter defaultConverter) {
        super(packageRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.packageRepository = packageRepository;
    }

    @Override
    public List<PackageDto> getByBusinessId(UUID id) {
        List<PackageDto> result = null;
        List<PackageEntity> entities = packageRepository.getByBusinessIdAndObjectState(id, ObjectState.ACTIVE);
        result = converter.convert(entities, dtoClass);
        setTags(result);
        return result;
    }

    @Override
    public Map<UUID, List<PackageDto>> getMapByBusinessIds(List<UUID> businessIds) {
        Map<UUID, List<PackageDto>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(businessIds)) {
            List<PackageEntity> entities = packageRepository.findAllByBusinessIdIn(businessIds);
            if (CollectionUtils.isNotEmpty(entities)) {
                result = entities.stream().map(i -> converter.convert(i, dtoClass)).collect(Collectors.groupingBy(PackageDto::getBusinessId));
            }
        }
        return result;
    }

    @Override
    public Map<UUID, List<LitePackageDto>> getLiteMapByBusinessIds(List<UUID> businessIds) {
        Map<UUID, List<LitePackageDto>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(businessIds)) {
            List<PackageEntity> entities = packageRepository.findAllByBusinessIdIn(businessIds);
            if (CollectionUtils.isNotEmpty(entities)) {
                result = entities.stream().map(i -> converter.convert(i, LitePackageDto.class)).collect(Collectors.groupingBy(LitePackageDto::getBusinessId));
            }
        }
        return result;
    }

    @Override
    public Map<UUID, LitePackageDto> getMapByIds(List<UUID> packageIds) {
        Map<UUID, LitePackageDto> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(packageIds)) {
            List<LitePackageDto> packages = this.getLiteByIds(packageIds);
            result = packages.stream().collect(Collectors.toMap(LitePackageDto::getId, i -> i));
        }
        return result;
    }
    
    @Override
    public List<LitePackageDto> getLiteByIds(List<UUID> ids) {
        List<LitePackageDto> result = null;
        if (CollectionUtils.isNotEmpty(ids)) {
            List<PackageEntity> entities = packageRepository.findAllById(ids);
            result = converter.convert(entities, LitePackageDto.class);
        }
        return result;
    }

    @Override
    public List<TagDto> addTag(UUID tagId, UUID idPackage) {
        checkPackageExist(idPackage);
        return objectTagService.addTag(tagId, idPackage);
    }

    @Override
    public List<TagDto> getTags(UUID idPackage) {
        return objectTagService.getByObjectId(idPackage);
    }

    @Override
    public List<TagDto> saveTags(List<UUID> tagId, UUID idPackage) {
        checkPackageExist(idPackage);
        return objectTagService.saveTags(tagId, idPackage);
    }

    @Override
    public List<TagDto> removeTag(UUID tagId, UUID idPackage) {
        checkPackageExist(idPackage);
        return objectTagService.removeTag(tagId, idPackage);
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
        setTags(Arrays.asList(result));
        return result;
    }

    @Override
    public List<LitePackageDto> getLitePackageByBusinessId(UUID id) {
        List<PackageEntity> entities = packageRepository.getByBusinessIdAndObjectState(id, ObjectState.ACTIVE);
        return converter.convert(entities, LitePackageDto.class);
    }

    @Override
    public List<PackageDto> getAll() {
        List<PackageDto> result = null;
        List<PackageEntity> entities = packageRepository.getAllByObjectState(ObjectState.ACTIVE);
        result = converter.convert(entities, dtoClass);
        setTags(result);
        return result;
    }

    @Override
    public PackageDto getById(UUID id) {
        PackageDto result = null;
        PackageEntity entity = packageRepository.findByIdAndObjectState(id, ObjectState.ACTIVE);
        result = converter.convert(entity, dtoClass);
        setTags(Arrays.asList(result));
        return result;
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
        setTags(result);
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
        businessEsService.indexAsync(result.getBusinessId());
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
        setTags(Arrays.asList(result));
        businessEsService.indexAsync(result.getBusinessId());
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
        businessEsService.indexAsync(dto.getBusinessId());
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
        businessPermissionFacade.checkPermissionByBusiness(dto.getBusinessId(), BusinessPermission.BUSINESS_ADMINISTRATION);
    }

    private void checkPackageExist(UUID idPackage) {
        if (!isExist(idPackage)) {
            throw new ClientException(PACKAGE_NOT_FOUND);
        }
    }

    private void setTags(List<PackageDto> packages) {
        if (CollectionUtils.isNotEmpty(packages)) {
            Set<UUID> ids = packages.stream().map(PackageDto::getId).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(ids)) {
                Map<UUID, List<TagDto>> map = objectTagService.getMapByObjectIds(new ArrayList<>(ids));
                packages.forEach(price -> {
                    price.setTags(map.get(price.getId()));
                });
            }
        }
    }
}
