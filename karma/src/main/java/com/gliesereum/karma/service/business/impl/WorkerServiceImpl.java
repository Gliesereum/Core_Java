package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.facade.GroupUserExchangeFacade;
import com.gliesereum.karma.model.entity.business.WorkerEntity;
import com.gliesereum.karma.model.repository.jpa.business.WorkerRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.business.WorkingSpaceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceDto;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        if (CollectionUtils.isNotEmpty(result)) {
            Map<UUID, UserDto> users = userExchangeService.findUserMapByIds(result.stream().map(LiteWorkerDto::getUserId).collect(Collectors.toList()));
            if (MapUtils.isNotEmpty(users)) {
                result.forEach(f -> f.setUser(users.get(f.getUserId())));
            }
        }
        return result;
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
            if (SecurityUtil.isAnonymous() || (!SecurityUtil.getUserCorporationIds().contains(corporationId) &&
                    !baseBusinessService.currentUserHavePermissionToActionInCorporationLikeWorker(corporationId))) {
                throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
            }
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
        Map<UUID, UserDto> users = userExchangeService.findUserMapByIds(result.stream().map(WorkerDto::getUserId).collect(Collectors.toList()));
        if (MapUtils.isNotEmpty(users)) {
            result.forEach(f -> f.setUser(users.get(f.getUserId())));
        }
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
    public WorkerDto create(WorkerDto dto) {
        checkWorker(dto.getUserId());
        checkWorkingSpace(dto);
        dto = super.create(dto);
        groupUserExchangeFacade.addUserByGroupPurposeAsync(dto.getUserId(), GroupPurpose.KARMA_WORKER);
        return dto;
    }

    @Override
    public WorkerDto update(WorkerDto dto) {
        checkWorker(dto.getUserId());
        checkWorkingSpace(dto);
        return super.update(dto);
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
        if (business != null) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        List<WorkingSpaceDto> workingSpaces = workingSpaceService.getByBusinessId(dto.getBusinessId(), false);
        if (CollectionUtils.isEmpty(workingSpaces)) {
            throw new ClientException(WORKING_SPACE_NOT_FOUND);
        }
        if (baseBusinessService.currentUserHavePermissionToActionInBusinessLikeOwner(dto.getBusinessId())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

}
