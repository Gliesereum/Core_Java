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
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceDto;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_ID_IS_EMPTY;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_FOUND;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class WorkerServiceImpl extends DefaultServiceImpl<WorkerDto, WorkerEntity> implements WorkerService {

    @Autowired
    private WorkerRepository repository;

    @Autowired
    private WorkingSpaceService workingSpaceService;

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    private GroupUserExchangeFacade groupUserExchangeFacade;

    private static final Class<WorkerDto> DTO_CLASS = WorkerDto.class;
    private static final Class<WorkerEntity> ENTITY_CLASS = WorkerEntity.class;

    public WorkerServiceImpl(WorkerRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<WorkerDto> getByWorkingSpaceId(UUID workingSpaceId) {
        if (workingSpaceId == null) {
            throw new ClientException(WORKING_SPACE_ID_IS_EMPTY);
        }
        List<WorkerEntity> entities = repository.findAllByWorkingSpaceId(workingSpaceId);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<WorkerDto> getByBusinessId(UUID businessId) {
        if (businessId == null) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        List<WorkerEntity> entities = repository.findAllByBusinessId(businessId);
        List<WorkerDto> result = converter.convert(entities, dtoClass);
        if (CollectionUtils.isNotEmpty(result)) {
            setUsers(result);
        }
        return result;
    }

    private void setUsers(List<WorkerDto> result) {
        Map<UUID, UserDto> users = userExchangeService.findUserMapByIds(result.stream().map(m -> m.getUserId()).collect(Collectors.toList()));
        if (MapUtils.isNotEmpty(users)) {
            result.forEach(f -> {
                f.setUser(users.get(f.getUserId()));
            });
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
        WorkerEntity entity = repository.findByUserIdAndBusinessId(userId, businessId);
        return converter.convert(entity, dtoClass);
    }

    @Override
    public List<WorkerDto> findByUserId(UUID userId) {
        if (userId == null) {
            throw new ClientException(USER_ID_IS_EMPTY);
        }
        List<WorkerEntity> entities = repository.findAllByUserId(userId);
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
        List<WorkingSpaceDto> workingSpaces = workingSpaceService.getByBusinessId(dto.getBusinessId());
        if (CollectionUtils.isEmpty(workingSpaces)) {
            throw new ClientException(WORKING_SPACE_NOT_FOUND);
        }
        if (baseBusinessService.currentUserHavePermissionToActionInBusinessLikeOwner(dto.getBusinessId())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

}
