package com.gliesereum.karma.service.business.impl;

import com.gliesereum.karma.model.entity.business.WorkingSpaceEntity;
import com.gliesereum.karma.model.repository.jpa.business.WorkingSpaceRepository;
import com.gliesereum.karma.service.business.BusinessCategoryFacade;
import com.gliesereum.karma.service.business.WorkingSpaceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.DIFFERENT_BUSINESS_OR_CATEGORY_OF_BUSINESS;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.WORKING_SPACE_INDEX_NUMBER_EXIST;

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
    private BusinessCategoryFacade businessCategoryFacade;

    @Autowired
    public WorkingSpaceServiceImpl(WorkingSpaceRepository workingSpaceRepository, DefaultConverter defaultConverter) {
        super(workingSpaceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.workingSpaceRepository = workingSpaceRepository;
    }

    @Override
    public List<WorkingSpaceDto> getByBusinessId(UUID businessId) {
        List<WorkingSpaceDto> result = null;
        if (businessId != null) {
            List<WorkingSpaceEntity> entities = workingSpaceRepository.findByBusinessId(businessId);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<LiteWorkingSpaceDto> getLiteWorkingSpaceByBusinessId(UUID id) {
        List<WorkingSpaceEntity> entities = workingSpaceRepository.findByBusinessId(id);
        return converter.convert(entities, LiteWorkingSpaceDto.class);
    }

    @Override
    public WorkingSpaceDto create(WorkingSpaceDto dto) {
        WorkingSpaceDto result = null;
        if (dto != null) {
            businessCategoryFacade.throwExceptionIfUserDontHavePermissionToAction(dto.getBusinessCategoryId(), dto.getBusinessId());
            dto = checkIndex(Arrays.asList(dto), dto.getBusinessId()).get(0);
            result = super.create(dto);
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
            businessCategoryFacade.throwExceptionIfUserDontHavePermissionToAction(dto.getBusinessCategoryId(), dto.getBusinessId());
            result = super.create(dtos);
        }
        return result;
    }

    @Override
    public WorkingSpaceDto update(WorkingSpaceDto dto) {
        WorkingSpaceDto result = null;
        if (dto != null) {
            businessCategoryFacade.throwExceptionIfUserDontHavePermissionToAction(dto.getBusinessCategoryId(), dto.getBusinessId());
            dto = checkIndex(Arrays.asList(dto), dto.getBusinessId()).get(0);
            result = super.update(dto);
        }
        return result;
    }

    @Override
    public void delete(UUID id) {
        if (id != null) {
            Optional<WorkingSpaceEntity> entity = repository.findById(id);
            entity.ifPresent(i -> {
                businessCategoryFacade.throwExceptionIfUserDontHavePermissionToAction(i.getBusinessCategoryId(), i.getBusinessId());
                repository.delete(i);
            });

        }
    }

    private List<WorkingSpaceDto> checkIndex(List<WorkingSpaceDto> workingSpaces, UUID businessId) {
        if (CollectionUtils.isNotEmpty(workingSpaces)) {
            List<WorkingSpaceEntity> existed = workingSpaceRepository.findByBusinessIdOrderByIndexNumberAsc(businessId);
            int lastIndex = 0;
            if (CollectionUtils.isNotEmpty(existed)) {
                lastIndex = existed.get(existed.size() - 1).getIndexNumber();
            }
            workingSpaces = workingSpaces.stream().sorted(Comparator.comparingInt(WorkingSpaceDto::getIndexNumber)).collect(Collectors.toList());
            for (WorkingSpaceDto workingSpace : workingSpaces) {
                if (workingSpace.getIndexNumber() == null) {
                    workingSpace.setIndexNumber(lastIndex + 1);
                }
                if (workingSpace.getIndexNumber() <= lastIndex) {
                    throw new ClientException(WORKING_SPACE_INDEX_NUMBER_EXIST);
                }
                lastIndex = workingSpace.getIndexNumber();
            }
        }
        return workingSpaces;
    }

}
