package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.model.entity.business.WorkTimeEntity;
import com.gliesereum.karma.model.repository.jpa.business.WorkTimeRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkTimeService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkTimeDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.WorkTimeType;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.TIME_FROM_CAN_NOT_BE_AFTER_TO_TIME;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class WorkTimeServiceImpl extends DefaultServiceImpl<WorkTimeDto, WorkTimeEntity> implements WorkTimeService {

    private static final Class<WorkTimeDto> DTO_CLASS = WorkTimeDto.class;
    private static final Class<WorkTimeEntity> ENTITY_CLASS = WorkTimeEntity.class;

    private final WorkTimeRepository workTimeRepository;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

    @Autowired
    private BusinessEsService businessEsService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private BaseBusinessService businessService;

    public WorkTimeServiceImpl(WorkTimeRepository workTimeRepository, DefaultConverter defaultConverter) {
        super(workTimeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.workTimeRepository = workTimeRepository;
    }

    @Override
    public List<WorkTimeDto> getByObjectId(UUID objectId) {
        List<WorkTimeDto> result = null;
        if (objectId != null) {
            List<WorkTimeEntity> entities = workTimeRepository.findByObjectId(objectId);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    @Transactional
    public WorkTimeDto create(WorkTimeDto dto) {
        WorkTimeDto result = null;
        if (dto != null) {
            checkOpportunityForCreateWorkTime(Arrays.asList(dto));
            checkDayExist(dto);
            checkPermission(dto.getType(), dto.getBusinessCategoryId(), dto.getObjectId());
            result = super.create(dto);
            indexAsync(dto.getType(), result.getObjectId());
        }
        return result;
    }

    @Override
    @Transactional
    public List<WorkTimeDto> create(List<WorkTimeDto> list) {
        List<WorkTimeDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            checkOpportunityForCreateWorkTime(list);
            WorkTimeDto dto = list.get(0);
            checkPermission(dto.getType(), dto.getBusinessCategoryId(), dto.getObjectId());
            list.forEach(this::checkDayExist);
            result = super.create(list);
            indexAsync(dto.getType(), dto.getObjectId());
        }
        return result;
    }

    @Override
    @Transactional
    public List<WorkTimeDto> update(List<WorkTimeDto> list) {
        List<WorkTimeDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            checkOpportunityForCreateWorkTime(list);
            WorkTimeDto dto = list.get(0);
            checkPermission(dto.getType(), dto.getBusinessCategoryId(), dto.getObjectId());
            result = super.update(list);
            indexAsync(dto.getType(), dto.getObjectId());
        }
        return result;
    }

    @Override
    public WorkTimeDto update(WorkTimeDto dto) {
        WorkTimeDto result = null;
        if (dto != null) {
            checkOpportunityForCreateWorkTime(Arrays.asList(dto));
            WorkTimeDto time = getById(dto.getId());
            if (time == null) {
                throw new ClientException(WORKING_TIME_NOT_FOUND);
            }
            if (!dto.getDayOfWeek().equals(time.getDayOfWeek())) {
                checkDayExist(dto);
            }
            checkPermission(dto.getType(), dto.getBusinessCategoryId(), dto.getObjectId());
            result = super.update(dto);
            indexAsync(dto.getType(), result.getObjectId());
        }
        return result;
    }

    @Override
    public void delete(UUID id, UUID businessCategoryId, WorkTimeType type) {
        if ((id != null) && (businessCategoryId != null)) {
            Optional<WorkTimeEntity> entity = repository.findById(id);
            entity.ifPresent(i -> {
                checkPermission(type, businessCategoryId, i.getObjectId());
                repository.delete(i);
                indexAsync(type, i.getObjectId());
            });
        }
    }

    @Override
    public void deleteByObjectId(UUID id) {
        workTimeRepository.deleteAllByObjectId(id);
    }

    @Override
    public void checkWorkTimesByBusyTime(List<WorkTimeDto> list, WorkerDto worker) {
        if (CollectionUtils.isNotEmpty(list) && worker != null) {
            checkWorkTimesCorrect(list);
            BaseBusinessDto business = businessService.getById(worker.getBusinessId());
            if (business == null) {
                throw new ClientException(BUSINESS_NOT_FOUND);
            }
            if (CollectionUtils.isEmpty(business.getWorkTimes())) {
                throw new ClientException(BUSINESS_NOT_HAVE_WORKING_TIME);
            }
            Map<DayOfWeek, WorkTimeDto> mapBusinessWorkTime = business.getWorkTimes().stream().collect(Collectors.toMap(WorkTimeDto::getDayOfWeek, w -> w));
            list.forEach(f -> {
                WorkTimeDto businessTimeWork = mapBusinessWorkTime.get(f.getDayOfWeek());
                if (businessTimeWork == null || !businessTimeWork.getIsWork() && f.getIsWork()) {
                    throw new ClientException("Business does not work in this day: ".concat(f.getDayOfWeek().toString()), 1434, 400);
                }
                if (f.getIsWork() && (businessTimeWork.getFrom().isAfter(f.getFrom()) ||
                        businessTimeWork.getTo().isBefore(f.getTo()))) {
                    throw new ClientException("Business work this day: ".concat(f.getDayOfWeek().toString())
                            .concat(" only from: ").concat(businessTimeWork.getFrom().toString())
                            .concat(" to: ").concat(businessTimeWork.getTo().toString()), 1435, 400);
                }
            });
            List<WorkerDto> workers = workerService.getByWorkingSpaceId(worker.getWorkingSpaceId());
            if (workers.size() > 1) {
                workers.stream().filter(filter -> !filter.getId().equals(worker.getId())).forEach(otherWorker -> {
                    List<WorkTimeDto> workTimes = otherWorker.getWorkTimes();
                    if (CollectionUtils.isNotEmpty(workTimes)) {
                        Map<DayOfWeek, WorkTimeDto> mapWorkerWorkTime = workTimes.stream().collect(Collectors.toMap(WorkTimeDto::getDayOfWeek, w -> w));
                        list.forEach(f -> {
                            WorkTimeDto workerTimeWork = mapWorkerWorkTime.get(f.getDayOfWeek());
                            if ((f.getIsWork() && workerTimeWork.getIsWork()) &&
                                    ((f.getFrom().plusMinutes(1).isAfter(workerTimeWork.getFrom()) && f.getFrom().minusMinutes(1).isBefore(workerTimeWork.getTo())) || (
                                            f.getTo().minusMinutes(1).isBefore(workerTimeWork.getTo()) && f.getTo().plusMinutes(1).isAfter(workerTimeWork.getFrom())))) {
                                throw new ClientException("This time is busy other worker on: ".concat(f.getDayOfWeek().toString())
                                        .concat(" from: ").concat(workerTimeWork.getFrom().toString())
                                        .concat(" to: ").concat(workerTimeWork.getTo().toString()), 1424, 403);
                            }
                        });
                    }
                });
            }
        }
    }

    private void checkPermission(WorkTimeType type, UUID categoryId, UUID objectId) {
        switch (type) {
            case WORKER: {
                //todo add facade for check worker time in business (now this check in worker service)
                break;
            }
            case BUSINESS: {
                businessPermissionFacade.checkPermissionByBusiness(objectId, BusinessPermission.BUSINESS_ADMINISTRATION);
                break;
            }
        }
    }

    private void indexAsync(WorkTimeType type, UUID objectId) {
        switch (type) {
            case WORKER: {
                break;
            }
            case BUSINESS: {
                businessEsService.indexAsync(objectId);
                break;
            }
        }
    }

    private void checkDayExist(WorkTimeDto dto) {
        if (workTimeRepository.existsByObjectIdAndDayOfWeek(dto.getObjectId(), dto.getDayOfWeek())) {
            throw new ClientException(WORKING_TIME_EXIST_IN_THIS_BUSINESS);
        }
    }

    private void checkOpportunityForCreateWorkTime(List<WorkTimeDto> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            WorkTimeDto dto = list.get(0);
            switch (dto.getType()) {
                case BUSINESS: {
                    checkWorkTimesCorrect(list);
                    break;
                }
                case WORKER: {
                    WorkerDto worker = workerService.getById(dto.getObjectId());
                    if (worker == null) {
                        throw new ClientException(WORKER_NOT_FOUND);
                    }
                    checkWorkTimesByBusyTime(list, worker);
                    break;
                }
            }
        }
    }

    private void checkWorkTimesCorrect(List<WorkTimeDto> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            if (list.stream().anyMatch(i -> !i.getObjectId().equals(list.get(0).getObjectId()))) {
                throw new ClientException(ALL_OBJECT_ID_NOT_EQUALS);
            }
            if (list.get(0).getType() == null) {
                throw new ClientException(TYPE_IS_NULL);
            }
            if (list.stream().anyMatch(i -> !i.getType().equals(list.get(0).getType()))) {
                throw new ClientException(ALL_TYPE_NOT_EQUALS);
            }
            if (list.stream().anyMatch(i -> i.getFrom().isAfter(i.getTo()))) {
                throw new ClientException(TIME_FROM_CAN_NOT_BE_AFTER_TO_TIME);
            }
        }
    }
}
