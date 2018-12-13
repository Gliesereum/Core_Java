package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.WorkTimeEntity;
import com.gliesereum.karma.model.repository.jpa.common.WorkTimeRepository;
import com.gliesereum.karma.service.common.WorkTimeService;
import com.gliesereum.karma.service.servicetype.ServiceTypeFacade;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.WorkTimeDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
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
 * @since 12/7/18
 */
@Slf4j
@Service
public class WorkTimeServiceImpl extends DefaultServiceImpl<WorkTimeDto, WorkTimeEntity> implements WorkTimeService {

    private static final Class<WorkTimeDto> DTO_CLASS = WorkTimeDto.class;
    private static final Class<WorkTimeEntity> ENTITY_CLASS = WorkTimeEntity.class;

    private final WorkTimeRepository workTimeRepository;

    @Autowired
    private ServiceTypeFacade serviceTypeFacade;

    public WorkTimeServiceImpl(WorkTimeRepository workTimeRepository, DefaultConverter defaultConverter) {
        super(workTimeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.workTimeRepository = workTimeRepository;
    }

    @Override
    public List<WorkTimeDto> getByBusinessServiceId(UUID businessServiceId) {
        List<WorkTimeDto> result = null;
        if (businessServiceId != null) {
            List<WorkTimeEntity> entities = workTimeRepository.findByBusinessServiceId(businessServiceId);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public WorkTimeDto create(WorkTimeDto dto) {
        WorkTimeDto result = null;
        if (dto != null) {
            serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(dto.getCarServiceType(), dto.getBusinessServiceId());
            result = super.create(dto);
        }
        return result;
    }

    @Override
    public WorkTimeDto update(WorkTimeDto dto) {
        WorkTimeDto result = null;
        if (dto != null) {
            serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(dto.getCarServiceType(), dto.getBusinessServiceId());
            result = super.update(dto);
        }
        return result;
    }

    @Override
    public void delete(UUID id, ServiceType serviceType) {
        if ((id != null) && (serviceType != null)) {
            Optional<WorkTimeEntity> entity = repository.findById(id);
            entity.ifPresent(i -> {
                serviceTypeFacade.throwExceptionIfUserDontHavePermissionToAction(serviceType, i.getBusinessServiceId());
                repository.delete(i);
            });

        }
    }
}
