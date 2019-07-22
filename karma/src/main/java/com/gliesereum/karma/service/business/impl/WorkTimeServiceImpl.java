package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.model.entity.business.WorkTimeEntity;
import com.gliesereum.karma.model.repository.jpa.business.WorkTimeRepository;
import com.gliesereum.karma.service.business.BusinessCategoryFacade;
import com.gliesereum.karma.service.business.WorkTimeService;
import com.gliesereum.karma.service.es.BusinessEsService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.business.WorkTimeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private BusinessCategoryFacade businessCategoryFacade;

    @Autowired
    private BusinessEsService businessEsService;

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
    public WorkTimeDto create(WorkTimeDto dto) {
        WorkTimeDto result = null;
        if (dto != null) {
            checkDayExist(dto);
            businessCategoryFacade.throwExceptionIfUserDontHavePermissionToAction(dto.getBusinessCategoryId(), dto.getObjectId());
            result = super.create(dto);
            businessEsService.indexAsync(result.getObjectId());
        }
        return result;
    }

    @Override
    public List<WorkTimeDto> create(List<WorkTimeDto> list) {
        List<WorkTimeDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            WorkTimeDto workTime = list.get(0);
            if (list.stream().anyMatch(i -> !i.getObjectId().equals(workTime.getObjectId()))) {
                throw new ClientException(ALL_OBJECT_ID_NOT_EQUALS);
            }
            businessCategoryFacade.throwExceptionIfUserDontHavePermissionToAction(workTime.getBusinessCategoryId(), workTime.getObjectId());
            list.forEach(this::checkDayExist);
            result = super.create(list);
            businessEsService.indexAsync(workTime.getObjectId());
        }
        return result;
    }

    @Override
    public List<WorkTimeDto> update(List<WorkTimeDto> list) {
        List<WorkTimeDto> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            WorkTimeDto workTime = list.get(0);
            if (list.stream().anyMatch(i -> !i.getObjectId().equals(workTime.getObjectId()))) {
                throw new ClientException(ALL_OBJECT_ID_NOT_EQUALS);
            }
            businessCategoryFacade.throwExceptionIfUserDontHavePermissionToAction(workTime.getBusinessCategoryId(), workTime.getObjectId());
            result = super.update(list);
            businessEsService.indexAsync(workTime.getObjectId());
        }
        return result;
    }

    @Override
    public WorkTimeDto update(WorkTimeDto dto) {
        WorkTimeDto result = null;
        if (dto != null) {
            WorkTimeDto time = getById(dto.getId());
            if (time == null) {
                throw new ClientException(WORKING_TIME_NOT_FOUND);
            }
            if (!dto.getDayOfWeek().equals(time.getDayOfWeek())) {
                checkDayExist(dto);
            }
            businessCategoryFacade.throwExceptionIfUserDontHavePermissionToAction(dto.getBusinessCategoryId(), dto.getObjectId());
            result = super.update(dto);
            businessEsService.indexAsync(result.getObjectId());
        }
        return result;
    }

    @Override
    public void delete(UUID id, UUID businessCategoryId) {
        if ((id != null) && (businessCategoryId != null)) {
            Optional<WorkTimeEntity> entity = repository.findById(id);
            entity.ifPresent(i -> {
                businessCategoryFacade.throwExceptionIfUserDontHavePermissionToAction(businessCategoryId, i.getObjectId());
                repository.delete(i);
                businessEsService.indexAsync(i.getObjectId());
            });
        }
    }

    @Override
    public void deleteByObjectId(UUID id){
        workTimeRepository.deleteAllByObjectId(id);
    }

    private void checkDayExist(WorkTimeDto dto) {
        if (workTimeRepository.existsByObjectIdAndDayOfWeek(dto.getObjectId(), dto.getDayOfWeek())) {
            throw new ClientException(WORKING_TIME_EXIST_IN_THIS_BUSINESS);
        }
    }
}
