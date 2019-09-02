package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.model.entity.business.WorkingSpaceEntity;
import com.gliesereum.karma.model.repository.jpa.business.WorkingSpaceRepository;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.business.WorkingSpaceService;
import com.gliesereum.karma.service.business.WorkingSpaceServicePriceService;
import com.gliesereum.karma.service.business.descriptions.WorkingSpaceDescriptionService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.business.descriptions.WorkingSpaceDescriptionDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class WorkingSpaceServiceImpl extends DefaultServiceImpl<WorkingSpaceDto, WorkingSpaceEntity> implements WorkingSpaceService {

    private static final Class<WorkingSpaceDto> DTO_CLASS = WorkingSpaceDto.class;
    private static final Class<WorkingSpaceEntity> ENTITY_CLASS = WorkingSpaceEntity.class;

    private final WorkingSpaceRepository workingSpaceRepository;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @Autowired
    private WorkingSpaceDescriptionService workingSpaceDescriptionService;

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    private WorkingSpaceServicePriceService workingSpaceServicePriceService;

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    public WorkingSpaceServiceImpl(WorkingSpaceRepository workingSpaceRepository, DefaultConverter defaultConverter) {
        super(workingSpaceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.workingSpaceRepository = workingSpaceRepository;
    }

    @Override
    public List<WorkingSpaceDto> getByBusinessId(UUID businessId, boolean setUsers) {
        List<WorkingSpaceDto> result = null;
        if (businessId != null) {
            List<WorkingSpaceEntity> entities = workingSpaceRepository.findByBusinessId(businessId);
            result = converter.convert(entities, dtoClass);
            if (setUsers && CollectionUtils.isNotEmpty(result)) {
                Set<UUID> userIds = new HashSet<>();
                result.forEach(f -> {
                    List<WorkerDto> workers = f.getWorkers();
                    if (CollectionUtils.isNotEmpty(workers)) {
                        userIds.addAll(workers.stream().map(m -> m.getUserId()).collect(Collectors.toSet()));
                    }
                });
                if (CollectionUtils.isNotEmpty(userIds)) {
                    Map<UUID, PublicUserDto> users = userExchangeService.findPublicUserMapByIds(userIds);
                    if (MapUtils.isNotEmpty(users)) {
                        result.forEach(f -> {
                            List<WorkerDto> workers = f.getWorkers();
                            if (CollectionUtils.isNotEmpty(workers)) {
                                workers.forEach(w -> w.setUser(users.get(w.getUserId())));
                            }
                        });
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<LiteWorkingSpaceDto> getLiteWorkingSpaceByBusinessId(UUID id) {
        List<WorkingSpaceEntity> entities = workingSpaceRepository.findByBusinessId(id);
        return converter.convert(entities, LiteWorkingSpaceDto.class);
    }

    @Override
    public List<WorkingSpaceServicePriceDto> addServicePrice(List<WorkingSpaceServicePriceDto> dtos) {
        List<WorkingSpaceServicePriceDto> result = null;
        if (CollectionUtils.isNotEmpty(dtos)) {
            Set<UUID> ids = dtos.stream().map(m -> m.getWorkingSpaceId()).collect(Collectors.toSet());
            List<WorkingSpaceEntity> entities = workingSpaceRepository.findAllById(ids);
            if (CollectionUtils.isNotEmpty(entities)) {
                Set<UUID> businessIds = entities.stream().map(m -> m.getBusinessId()).collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(businessIds) && businessIds.size() > 1) {
                    throw new ClientException(TRY_CHANGE_DIFFERENT_BUSINESS);
                }
                UUID businessId = businessIds.iterator().next();
                businessPermissionFacade.checkPermissionByBusiness(businessId, BusinessPermission.BUSINESS_ADMINISTRATION);
                Set<UUID> servicePriceIds = dtos.stream().map(m -> m.getPriceId()).collect(Collectors.toSet());
                int count = servicePriceService.getCountByBusinessIdAndServicePriceIds(businessId, new ArrayList<>(servicePriceIds));
                if (servicePriceIds.size() != count) {
                    throw new ClientException(SERVICE_NOT_FOUND);
                }
                result = workingSpaceServicePriceService.create(dtos);
            }
        }
        return result;
    }

    @Override
    public LiteWorkingSpaceDto getLiteWorkingSpaceById(UUID workingSpaceId) {
        WorkingSpaceEntity entity = repository.getOne(workingSpaceId);
        return converter.convert(entity, LiteWorkingSpaceDto.class);
    }

    @Override
    public Map<UUID, LiteWorkingSpaceDto> getLiteWorkingSpaceMapByIds(Collection<UUID> collect) {
        Map<UUID, LiteWorkingSpaceDto> result = new HashMap<>();
        List<LiteWorkingSpaceDto> list = getLiteWorkingSpaceByIds(new ArrayList<>(collect));
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.stream().collect(Collectors.toMap(LiteWorkingSpaceDto::getId, i -> i));
        }
        return result;
    }

    @Override
    public boolean isExistByIdAndBusinessId(UUID id, UUID businessId) {
        return workingSpaceRepository.existsByIdAndBusinessId(id, businessId);
    }

    private List<LiteWorkingSpaceDto> getLiteWorkingSpaceByIds(List<UUID> ids) {
        List<WorkingSpaceEntity> entities = repository.findAllById(ids);
        return converter.convert(entities, LiteWorkingSpaceDto.class);
    }

    @Override
    public WorkingSpaceDto create(WorkingSpaceDto dto) {
        WorkingSpaceDto result = null;
        if (dto != null) {
            businessPermissionFacade.checkPermissionByBusiness(dto.getBusinessId(), BusinessPermission.BUSINESS_ADMINISTRATION);
            dto = checkIndex(Arrays.asList(dto), dto.getBusinessId()).get(0);
            result = super.create(dto);
            List<WorkingSpaceDescriptionDto> descriptions = workingSpaceDescriptionService.create(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
        }
        return result;
    }

    @Override
    public List<WorkingSpaceDto> create(List<WorkingSpaceDto> dtos) {
        List<WorkingSpaceDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(dtos)) {
            WorkingSpaceDto dto = dtos.get(0);
            if (!dtos.stream().allMatch(w -> w.getBusinessId().equals(dto.getBusinessId())) ||
                    !dtos.stream().allMatch(w -> w.getBusinessCategoryId().equals(dto.getBusinessCategoryId()))) {
                throw new ClientException(DIFFERENT_BUSINESS_OR_CATEGORY_OF_BUSINESS);
            }
            dtos = checkIndex(dtos, dtos.get(0).getBusinessId());
            businessPermissionFacade.checkPermissionByBusiness(dto.getBusinessId(), BusinessPermission.BUSINESS_ADMINISTRATION);
            result = super.create(dtos);
            for (int i = 0; i < result.size(); i++) {
                List<WorkingSpaceDescriptionDto> descriptions = workingSpaceDescriptionService.create(dtos.get(i).getDescriptions(), result.get(0).getId());
                result.get(0).setDescriptions(descriptions);
            }
        }
        return result;
    }

    @Override
    public WorkingSpaceDto update(WorkingSpaceDto dto) {
        WorkingSpaceDto result = null;
        if (dto != null) {
            businessPermissionFacade.checkPermissionByBusiness(dto.getBusinessId(), BusinessPermission.BUSINESS_ADMINISTRATION);
            WorkingSpaceDto saved = getById(dto.getId());
            dto.setIndexNumber(saved.getIndexNumber());
            result = super.update(dto);
            if (result != null && CollectionUtils.isNotEmpty(result.getWorkers())) {
                Set<UUID> userIds = result.getWorkers().stream().map(m -> m.getUserId()).collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(userIds)) {
                    Map<UUID, PublicUserDto> users = userExchangeService.findPublicUserMapByIds(userIds);
                    if (MapUtils.isNotEmpty(users)) {
                        result.getWorkers().forEach(w -> w.setUser(users.get(w.getUserId())));
                    }
                }
            }
            List<WorkingSpaceDescriptionDto> descriptions = workingSpaceDescriptionService.update(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
        }
        return result;
    }

    @Override
    public void delete(UUID id) {
        if (id != null) {
            Optional<WorkingSpaceEntity> entity = repository.findById(id);
            entity.ifPresent(i -> {
                businessPermissionFacade.checkPermissionByBusiness(i.getBusinessId(), BusinessPermission.BUSINESS_ADMINISTRATION);
                repository.delete(i);
            });
            List<WorkerDto> workers = workerService.getByWorkingSpaceId(id);
            if (CollectionUtils.isNotEmpty(workers)) {
                workers.forEach(f->f.setWorkingSpaceId(null));
                workerService.update(workers);
            }
        }
    }

    private List<WorkingSpaceDto> checkIndex(List<WorkingSpaceDto> workingSpaces, UUID businessId) {
        if (CollectionUtils.isNotEmpty(workingSpaces)) {
            int lastIndex = 0;
            List<WorkingSpaceEntity> existed = workingSpaceRepository.findByBusinessId(businessId);
            if (CollectionUtils.isNotEmpty(existed)) {
                lastIndex = existed.size();
            }
            for (WorkingSpaceDto workingSpace : workingSpaces) {
                workingSpace.setIndexNumber(lastIndex += 1);
            }
        }
        return workingSpaces;
    }

}
