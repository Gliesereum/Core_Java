package com.gliesereum.karma.service.service.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.model.entity.service.ServicePriceEntity;
import com.gliesereum.karma.model.repository.jpa.service.ServicePriceRepository;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.karma.service.filter.FilterAttributeService;
import com.gliesereum.karma.service.filter.FilterService;
import com.gliesereum.karma.service.filter.PriceFilterAttributeService;
import com.gliesereum.karma.service.service.PackageService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.karma.service.service.ServiceService;
import com.gliesereum.karma.service.service.descriptions.ServicePriceDescriptionService;
import com.gliesereum.karma.service.tag.ObjectTagService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.filter.FilterAttributeDto;
import com.gliesereum.share.common.model.dto.karma.filter.FilterDto;
import com.gliesereum.share.common.model.dto.karma.filter.PriceFilterAttributeDto;
import com.gliesereum.share.common.model.dto.karma.service.LiteServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.model.dto.karma.service.ServiceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.ServicePriceDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.tag.TagDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class ServicePriceServiceImpl extends DefaultServiceImpl<ServicePriceDto, ServicePriceEntity> implements ServicePriceService {

    private static final Class<ServicePriceDto> DTO_CLASS = ServicePriceDto.class;
    private static final Class<ServicePriceEntity> ENTITY_CLASS = ServicePriceEntity.class;

    private final ServicePriceRepository servicePriceRepository;

    @Autowired
    private PackageService packageService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @Autowired
    private PriceFilterAttributeService priceFilterAttributeService;

    @Autowired
    private FilterAttributeService filterAttributeService;

    @Autowired
    private FilterService filterService;

    @Autowired
    private BusinessEsService businessEsService;

    @Autowired
    private ServicePriceDescriptionService servicePriceDescriptionService;

    @Autowired
    private ObjectTagService objectTagService;

    public ServicePriceServiceImpl(ServicePriceRepository servicePriceRepository, DefaultConverter defaultConverter) {
        super(servicePriceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.servicePriceRepository = servicePriceRepository;
    }

    @Override
    public Map<UUID, LiteServicePriceDto> getMapByIds(List<UUID> servicePriceIds) {
        Map<UUID, LiteServicePriceDto> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(servicePriceIds)) {

            List<LiteServicePriceDto> services = this.getLiteByIds(servicePriceIds);
            if (CollectionUtils.isNotEmpty(services)) {
                result = services.stream().collect(Collectors.toMap(LiteServicePriceDto::getId, i -> i));
            }
        }
        return result;
    }

    @Override
    public List<LiteServicePriceDto> getLiteByIds(List<UUID> ids) {
        List<LiteServicePriceDto> result = null;
        if (CollectionUtils.isNotEmpty(ids)) {
            List<ServicePriceEntity> entities = servicePriceRepository.findAllById(ids);
            result = converter.convert(entities, LiteServicePriceDto.class);
        }
        return result;
    }

    @Override
    public List<TagDto> addTag(UUID tagId, UUID idPrice) {
        checkPriceExist(idPrice);
        return objectTagService.addTag(tagId, idPrice);
    }

    @Override
    public List<TagDto> getTags(UUID idPrice) {
        return objectTagService.getByObjectId(idPrice);
    }

    @Override
    public List<TagDto> saveTags(List<UUID> tagId, UUID idPrice) {
        checkPriceExist(idPrice);
        return objectTagService.saveTags(tagId, idPrice);
    }

    @Override
    public List<TagDto> removeTag(UUID tagId, UUID idPrice) {
        checkPriceExist(idPrice);
        return objectTagService.removeTag(tagId, idPrice);
    }

    @Override
    public ServicePriceDto create(ServicePriceDto dto) {
        ServicePriceDto result = null;
        if (dto != null) {
            checkPermission(dto);
            dto.setObjectState(ObjectState.ACTIVE);
            result = super.create(setCustomName(dto));
            List<ServicePriceDescriptionDto> descriptions = servicePriceDescriptionService.create(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
            businessEsService.indexAsync(result.getBusinessId());
        }
        return result;
    }

    @Override
    @Transactional
    public ServicePriceDto update(ServicePriceDto dto) {
        ServicePriceDto result = null;
        if (dto != null) {
            checkPermission(dto);
            ServicePriceDto oldDto = getById(dto.getId());
            if (oldDto == null) {
                throw new ClientException(SERVICE_NOT_FOUND);
            }
            dto.setObjectState(oldDto.getObjectState());
            result = super.update(setCustomName(dto));
            List<ServicePriceDescriptionDto> descriptions = servicePriceDescriptionService.update(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
            businessEsService.indexAsync(result.getBusinessId());
            setTags(Arrays.asList(result));
        }
        return result;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        ServicePriceDto dto = getPrice(id);
        dto.setObjectState(ObjectState.DELETED);
        ServicePriceDto result = super.update(dto);
        businessEsService.indexAsync(result.getBusinessId());
    }

    @Override
    public List<ServicePriceDto> getAll() {
        List<ServicePriceDto> result = null;
        List<ServicePriceEntity> entities = servicePriceRepository.getAllByObjectState(ObjectState.ACTIVE);
        result = converter.convert(entities, dtoClass);
        setTags(result);
        return result;
    }

    @Override
    public ServicePriceDto getById(UUID id) {
        ServicePriceDto result = null;
        ServicePriceEntity entity = servicePriceRepository.findByIdAndObjectState(id, ObjectState.ACTIVE);
        result = converter.convert(entity, dtoClass);
        setTags(Arrays.asList(result));
        return result;
    }

    @Override
    public ServicePriceDto getByIdAndRefresh(UUID id) {
        ServicePriceDto result = null;
        ServicePriceEntity entity = servicePriceRepository.findByIdAndObjectState(id, ObjectState.ACTIVE);
        if (entity != null) {
            servicePriceRepository.refresh(entity);
        }
        result = converter.convert(entity, dtoClass);
        setTags(Arrays.asList(result));
        return result;
    }

    @Override
    public List<LiteServicePriceDto> getLiteServicePriceByBusinessId(UUID id) {
        List<ServicePriceEntity> entities = servicePriceRepository.findAllByBusinessIdAndObjectState(id, ObjectState.ACTIVE);
        return converter.convert(entities, LiteServicePriceDto.class);
    }

    @Override
    public int getCountByBusinessIdAndServicePriceIds(UUID businessId, List<UUID> servicePriceIds) {
        return servicePriceRepository.countAllByBusinessIdAndIdIn(businessId, servicePriceIds);
    }

    @Override
    public List<ServicePriceDto> getByIds(Iterable<UUID> ids) {
        List<ServicePriceDto> result = null;
        if (ids != null) {
            List<ServicePriceEntity> entities = servicePriceRepository.getAllByIdInAndObjectState(ids, ObjectState.ACTIVE);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<ServicePriceDto> getAllByPackage(UUID id) {
        List<ServicePriceDto> result = Collections.emptyList();
        PackageDto packageDto = packageService.getById(id);
        if (packageDto != null && CollectionUtils.isNotEmpty(packageDto.getServices())) {
            result = packageDto.getServices().stream().filter(f -> f.getObjectState().equals(ObjectState.ACTIVE)).collect(Collectors.toList());
        }
        setTags(result);
        return result;
    }

    @Override
    public List<ServicePriceDto> getByBusinessId(UUID id) {
        List<ServicePriceDto> result = null;
        List<ServicePriceEntity> entities = servicePriceRepository.findAllByBusinessIdAndObjectState(id, ObjectState.ACTIVE);
        result = converter.convert(entities, dtoClass);
        setTags(result);
        return result;
    }

    @Override
    public List<ServicePriceDto> getByBusinessIds(List<UUID> ids) {
        List<ServicePriceDto> result = null;
        List<ServicePriceEntity> entities = servicePriceRepository.findAllByBusinessIdInAndObjectState(ids, ObjectState.ACTIVE);
        result = converter.convert(entities, dtoClass);
        setTags(result);
        return result;
    }

    @Override
    public Map<UUID, List<ServicePriceDto>> getMapByBusinessIds(List<UUID> ids) {
        Map<UUID, List<ServicePriceDto>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            List<ServicePriceDto> prices = getByBusinessIds(ids);
            if (CollectionUtils.isNotEmpty(prices)) {
                result = prices.stream().collect(Collectors.groupingBy(ServicePriceDto::getBusinessId, Collectors.toList()));
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void addFilterAttribute(UUID idPrice, UUID idAttribute) {
        ServicePriceDto price = getPrice(idPrice);
        checkFilterAttribute(idAttribute, price.getService().getBusinessCategoryId());
        priceFilterAttributeService.create(new PriceFilterAttributeDto(idPrice, idAttribute));
        businessEsService.indexAsync(price.getBusinessId());
    }

    @Override
    @Transactional
    public ServicePriceDto addFilterAttributes(UUID idPrice, List<UUID> idsAttribute) {
        ServicePriceDto price = getPrice(idPrice);
        List<PriceFilterAttributeDto> list = new ArrayList<>();
        priceFilterAttributeService.deleteByPriceId(idPrice);
        price.setAttributes(new ArrayList<>());
        if (CollectionUtils.isNotEmpty(idsAttribute)) {
            idsAttribute.forEach(f -> {
                checkFilterAttribute(f, price.getService().getBusinessCategoryId());
                list.add(new PriceFilterAttributeDto(idPrice, f));
                price.getAttributes().add(filterAttributeService.getById(f));
            });
            priceFilterAttributeService.create(list);
        }
        businessEsService.indexAsync(price.getBusinessId());
        return price;
    }

    @Override
    @Transactional
    public void removeFilterAttribute(UUID idPrice, UUID idAttribute) {
        ServicePriceDto price = getPrice(idPrice);
        checkFilterAttribute(idAttribute);
        checkFilterAttributeExist(idPrice, idAttribute);
        priceFilterAttributeService.deleteByPriceIdAndFilterId(idPrice, idAttribute);
        businessEsService.indexAsync(price.getBusinessId());
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
        businessPermissionFacade.checkPermissionByBusiness(dto.getBusinessId(), BusinessPermission.BUSINESS_ADMINISTRATION);
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

    private void checkFilterAttribute(UUID idAttribute, UUID businessCategoryId) {
        FilterAttributeDto attribute = filterAttributeService.getById(idAttribute);
        if (attribute == null) {
            throw new ClientException(FILTER_ATTRIBUTE_NOT_FOUND);
        }
        FilterDto filter = filterService.getById(attribute.getFilterId());
        if (!filter.getBusinessCategoryId().equals(businessCategoryId)) {
            throw new ClientException(FILTER_ATTRIBUTE_NOT_FOUND_BY_SERVICE_TYPE);
        }
    }

    private void checkFilterAttribute(UUID idAttribute) {
        FilterAttributeDto attribute = filterAttributeService.getById(idAttribute);
        if (attribute == null) {
            throw new ClientException(FILTER_ATTRIBUTE_NOT_FOUND);
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

    private void checkPriceExist(UUID idPrice) {
        if (!isExist(idPrice)) {
            throw new ClientException(SERVICE_PRICE_NOT_FOUND);
        }
    }

    private void setTags(List<ServicePriceDto> prices) {
        if (CollectionUtils.isNotEmpty(prices)) {
            Set<UUID> ids = prices.stream().map(ServicePriceDto::getId).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(ids)) {
                Map<UUID, List<TagDto>> map = objectTagService.getMapByObjectIds(new ArrayList<>(ids));
                prices.forEach(price -> {
                    price.setTags(map.get(price.getId()));
                });
            }
        }
    }
}
