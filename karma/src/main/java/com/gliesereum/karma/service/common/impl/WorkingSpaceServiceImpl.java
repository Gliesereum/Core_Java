package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.WorkingSpaceEntity;
import com.gliesereum.karma.model.repository.jpa.common.WorkingSpaceRepository;
import com.gliesereum.karma.service.common.WorkingSpaceService;
import com.gliesereum.karma.service.servicetype.ServiceTypeFacade;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.WorkingSpaceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/17/18
 */
@Slf4j
@Service
public class WorkingSpaceServiceImpl extends DefaultServiceImpl<WorkingSpaceDto, WorkingSpaceEntity> implements WorkingSpaceService {

    private static final Class<WorkingSpaceDto> DTO_CLASS = WorkingSpaceDto.class;
    private static final Class<WorkingSpaceEntity> ENTITY_CLASS = WorkingSpaceEntity.class;

    private final WorkingSpaceRepository workTimeRepository;

    @Autowired
    private ServiceTypeFacade serviceTypeFacade;

    public WorkingSpaceServiceImpl(WorkingSpaceRepository workTimeRepository, DefaultConverter defaultConverter) {
        super(workTimeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.workTimeRepository = workTimeRepository;
    }

    @Override
    public List<WorkingSpaceDto> getByCorporationServiceId(UUID corporationServiceId) {
        List<WorkingSpaceDto> result = null;
        if (corporationServiceId != null) {
            List<WorkingSpaceEntity> entities = workTimeRepository.findByCorporationServiceId(corporationServiceId);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public WorkingSpaceDto create(WorkingSpaceDto dto) {
        WorkingSpaceDto result = null;
        if (dto != null) {
            serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(dto.getCarServiceType(), dto.getCorporationServiceId());
            result = super.create(dto);
        }
        return result;
    }

    @Override
    public WorkingSpaceDto update(WorkingSpaceDto dto) {
        WorkingSpaceDto result = null;
        if (dto != null) {
            serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(dto.getCarServiceType(), dto.getCorporationServiceId());
            result = super.update(dto);
        }
        return result;
    }

    @Override
    public void delete(UUID id) {
        if (id != null) {
            Optional<WorkingSpaceEntity> entity = repository.findById(id);
            entity.ifPresent(i -> {
                serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(i.getCarServiceType(), i.getCorporationServiceId());
                repository.delete(i);
            });

        }
    }

}
