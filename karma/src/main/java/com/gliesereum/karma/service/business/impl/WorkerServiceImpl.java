package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.model.entity.business.WorkerEntity;
import com.gliesereum.karma.model.repository.jpa.business.WorkerRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.business.WorkingSpaceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;
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
    public WorkerDto create(WorkerDto dto){
        checkWorker(dto.getWorkerId());
        checkWorkingSpace(dto.getWorkingSpaceId());
        return super.create(dto);
    }

    @Override
    public WorkerDto update(WorkerDto dto){
        checkWorker(dto.getWorkerId());
        checkWorkingSpace(dto.getWorkingSpaceId());
        return super.update(dto);
    }

    private void checkWorker(UUID workerId){
        if (workerId == null) {
            throw new ClientException(WORKER_ID_IS_EMPTY);
        }
        if (!userExchangeService.userIsExist(workerId)) {
            throw new ClientException(USER_NOT_FOUND);
        }
    }

    private void checkWorkingSpace(UUID workingSpaceId) {
        if (workingSpaceId == null) {
            throw new ClientException(WORKING_SPACE_ID_IS_EMPTY);
        }
        WorkingSpaceDto workingSpace = workingSpaceService.getById(workingSpaceId);
        if (workingSpace == null) {
            throw new ClientException(WORKING_SPACE_NOT_FOUND);
        }
        if (baseBusinessService.currentUserHavePermissionToActionInBusiness(workingSpace.getBusinessId())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
    }

}
