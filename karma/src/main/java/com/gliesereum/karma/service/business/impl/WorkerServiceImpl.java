package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.facade.group.GroupUserExchangeFacade;
import com.gliesereum.karma.facade.worker.WorkerFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.model.entity.business.WorkerEntity;
import com.gliesereum.karma.model.repository.jpa.business.WorkerRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkTimeService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.business.WorkingSpaceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.business.*;
import com.gliesereum.share.common.model.dto.karma.enumerated.WorkTimeType;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class WorkerServiceImpl extends DefaultServiceImpl<WorkerDto, WorkerEntity> implements WorkerService {

    private static final Class<WorkerDto> DTO_CLASS = WorkerDto.class;
    private static final Class<WorkerEntity> ENTITY_CLASS = WorkerEntity.class;

    private final WorkerRepository workerRepository;

    @Autowired
    private WorkingSpaceService workingSpaceService;

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    private GroupUserExchangeFacade groupUserExchangeFacade;

    @Autowired
    private WorkerFacade workerFacade;

    @Autowired
    private WorkTimeService workTimeService;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @Autowired
    public WorkerServiceImpl(WorkerRepository workerRepository, DefaultConverter defaultConverter) {
        super(workerRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.workerRepository = workerRepository;
    }

    @Override
    public List<WorkerDto> getByWorkingSpaceId(UUID workingSpaceId) {
        if (workingSpaceId == null) {
            throw new ClientException(WORKING_SPACE_ID_IS_EMPTY);
        }
        List<WorkerEntity> entities = workerRepository.findAllByWorkingSpaceId(workingSpaceId);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<WorkerDto> getByBusinessId(UUID businessId) {
        if (businessId == null) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        List<WorkerEntity> entities = workerRepository.findAllByBusinessId(businessId);
        List<WorkerDto> result = converter.convert(entities, dtoClass);
        if (CollectionUtils.isNotEmpty(result)) {
            setUsers(result);
        }
        return result;
    }

    @Override
    public List<LiteWorkerDto> getLiteWorkerByBusinessId(UUID id) {
        List<WorkerEntity> entities = workerRepository.findAllByBusinessId(id);
        List<LiteWorkerDto> result = converter.convert(entities, LiteWorkerDto.class);
        return setUsersInLiteModels(result);
    }

    @Override
    public List<LiteWorkerDto> getLiteWorkerByIds(List<UUID> ids) {
        List<WorkerEntity> entities = workerRepository.findAllById(ids);
        List<LiteWorkerDto> result = converter.convert(entities, LiteWorkerDto.class);
        return setUsersInLiteModels(result);
    }

    private List<LiteWorkerDto> setUsersInLiteModels(List<LiteWorkerDto> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            Map<UUID, UserDto> users = userExchangeService.findUserMapByIds(list.stream().map(LiteWorkerDto::getUserId).collect(Collectors.toList()));
            if (MapUtils.isNotEmpty(users)) {
                list.forEach(f -> f.setUser(users.get(f.getUserId())));
            }
        }
        return list;
    }

    @Override
    public List<WorkerDto> findByUserIdAndCorporationId(UUID userId, UUID corporationId) {
        if (corporationId == null) {
            throw new ClientException(CORPORATION_ID_IS_EMPTY);
        }
        if (userId == null) {
            throw new ClientException(USER_ID_IS_EMPTY);
        }
        List<WorkerDto> result = new ArrayList<>();
        List<UUID> businessIds = baseBusinessService.getIdsByCorporationIds(Arrays.asList(corporationId));
        if (CollectionUtils.isNotEmpty(businessIds)) {
            List<WorkerEntity> entities = workerRepository.findByUserIdAndBusinessIdIn(userId, businessIds);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<WorkerDto> getByCorporationId(UUID corporationId) {
        List<WorkerDto> result = new ArrayList<>();
        if (corporationId != null) {
            businessPermissionFacade.checkPermissionByCorporation(corporationId, BusinessPermission.VIEW_BUSINESS_INFO);
            List<UUID> businessIds = baseBusinessService.getIdsByCorporationIds(Arrays.asList(corporationId));
            if (CollectionUtils.isNotEmpty(businessIds)) {
                List<WorkerEntity> entities = workerRepository.findAllByBusinessIdIn(businessIds);
                result = converter.convert(entities, dtoClass);
                if (CollectionUtils.isNotEmpty(result)) {
                    setUsers(result);
                    result.sort(Comparator.comparing(WorkerDto::getBusinessId));
                }
            }
        }
        return result;
    }

    @Override
    public void setUsers(List<WorkerDto> result) {
        Map<UUID, PublicUserDto> users = userExchangeService.findPublicUserMapByIds(result.stream().map(WorkerDto::getUserId).collect(Collectors.toList()));
        if (MapUtils.isNotEmpty(users)) {
            result.forEach(f -> f.setUser(users.get(f.getUserId())));
        }
    }

    @Override
    public boolean existByUserIdAndCorporationId(UUID userId, UUID corporationId) {
        boolean result = false;
        if (ObjectUtils.allNotNull(userId, corporationId)) {
            result = workerRepository.existsByUserIdAndCorporationId(userId, corporationId);
        }
        return result;
    }

    @Override
    public boolean existByUserIdAndBusinessId(UUID userId, UUID businessId) {
        boolean result = false;
        if (ObjectUtils.allNotNull(userId, businessId)) {
            result = workerRepository.existsByUserIdAndAndBusinessId(userId, businessId);
        }
        return result;
    }

    @Override
    public LiteWorkerDto getLiteWorkerById(UUID workerId) {
        WorkerEntity entity = repository.getOne(workerId);
        return converter.convert(entity, LiteWorkerDto.class);
    }

    @Override
    public Map<UUID, LiteWorkerDto> getLiteWorkerMapByIds(Collection<UUID> collect) {
        Map<UUID, LiteWorkerDto> result = new HashMap<>();
        List<LiteWorkerDto> list = getLiteWorkerByIds(new ArrayList<>(collect));
        if (CollectionUtils.isNotEmpty(list)) {
            result = list.stream().collect(Collectors.toMap(LiteWorkerDto::getId, i -> i));
        }
        return result;
    }

    @Override
    @Transactional
    public WorkerDto createWorkerWithUser(WorkerDto worker) {
        WorkerDto result = null;
        if (worker != null && worker.getUser() != null) {
            PublicUserDto user = userExchangeService.createOrGetPublicUser(worker.getUser());
            if (user != null) {
                worker.setUserId(user.getId());
                result = create(worker);
                result.setUser(user);
                workerFacade.sendMessageToWorkerAfterCreate(result);
            }
        }
        return result;
    }

    @Override
    public WorkerDto findByUserIdAndBusinessId(UUID userId, UUID businessId) {
        if (businessId == null) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        if (userId == null) {
            throw new ClientException(USER_ID_IS_EMPTY);
        }
        WorkerEntity entity = workerRepository.findByUserIdAndBusinessId(userId, businessId);
        return converter.convert(entity, dtoClass);
    }

    @Override
    public List<WorkerDto> findByUserId(UUID userId) {
        if (userId == null) {
            throw new ClientException(USER_ID_IS_EMPTY);
        }
        List<WorkerEntity> entities = workerRepository.findAllByUserId(userId);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public WorkerDto create(WorkerDto dto) {
        checkWorker(dto.getUserId());
        checkWorkingSpace(dto);
        dto = super.create(dto);
        createWorkTimes(dto);
        groupUserExchangeFacade.addUserByGroupPurposeAsync(dto.getUserId(), GroupPurpose.KARMA_WORKER);
        return dto;
    }

    @Override
    @Transactional
    public WorkerDto update(WorkerDto dto) {
        checkWorker(dto.getUserId());
        checkWorkingSpace(dto);
        workTimeService.deleteByObjectId(dto.getId());
        createWorkTimes(dto);
        return super.update(dto);
    }

    private void createWorkTimes(WorkerDto dto) {
        if (CollectionUtils.isNotEmpty(dto.getWorkTimes())) {
            dto.getWorkTimes().forEach(f -> {
                f.setObjectId(dto.getId());
                f.setType(WorkTimeType.WORKER);
            });
            List<WorkTimeDto> workTimes = workTimeService.create(dto.getWorkTimes());
            dto.setWorkTimes(workTimes);
        }
    }

    @Override
    public boolean checkWorkerExistByPhone(String phone) {
        boolean result = false;
        if (StringUtils.isNotBlank(phone)) {
            UserDto user = userExchangeService.getByPhone(phone);
            if (user != null) {
                List<WorkerDto> worker = findByUserId(user.getId());
                if (CollectionUtils.isNotEmpty(worker)) {
                    result = true;
                } else {
                    List<BaseBusinessDto> business = baseBusinessService.getByCorporationIds(user.getCorporationIds());
                    if (CollectionUtils.isNotEmpty(business)) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }

    private void checkWorker(UUID userId) {
        if (userId == null) {
            throw new ClientException(USER_ID_IS_EMPTY);
        }
        if (!userExchangeService.userIsExist(userId)) {
            throw new ClientException(USER_NOT_FOUND);
        }
    }

    private void checkWorkingSpace(WorkerDto dto) {
        if (dto.getWorkingSpaceId() == null) {
            throw new ClientException(WORKING_SPACE_ID_IS_EMPTY);
        }
        if (dto.getBusinessId() == null) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        BaseBusinessDto business = baseBusinessService.getById(dto.getBusinessId());
        if (business == null) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        dto.setCorporationId(business.getCorporationId());
        List<WorkingSpaceDto> workingSpaces = workingSpaceService.getByBusinessId(dto.getBusinessId(), false);
        if (CollectionUtils.isEmpty(workingSpaces)) {
            throw new ClientException(WORKING_SPACE_NOT_FOUND);
        }
        businessPermissionFacade.checkPermissionByBusiness(dto.getBusinessId(), BusinessPermission.BUSINESS_ADMINISTRATION);

    }

}
