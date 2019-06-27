package com.gliesereum.karma.service.business.descriptions.impl;

import com.gliesereum.karma.model.entity.business.descriptions.WorkingSpaceDescriptionEntity;
import com.gliesereum.karma.model.repository.jpa.business.descriptions.WorkingSpaceDescriptionRepository;
import com.gliesereum.karma.service.business.descriptions.WorkingSpaceDescriptionService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.business.descriptions.WorkingSpaceDescriptionDto;
import com.gliesereum.share.common.service.descrption.impl.DefaultDescriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class WorkingSpaceDescriptionServiceImpl extends DefaultDescriptionServiceImpl<WorkingSpaceDescriptionDto, WorkingSpaceDescriptionEntity> implements WorkingSpaceDescriptionService {

    private static final Class<WorkingSpaceDescriptionDto> DTO_CLASS = WorkingSpaceDescriptionDto.class;
    private static final Class<WorkingSpaceDescriptionEntity> ENTITY_CLASS = WorkingSpaceDescriptionEntity.class;

    private final WorkingSpaceDescriptionRepository workingSpaceDescriptionRepository;

    @Autowired
    public WorkingSpaceDescriptionServiceImpl(WorkingSpaceDescriptionRepository workingSpaceDescriptionRepository, DefaultConverter defaultConverter) {
        super(workingSpaceDescriptionRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.workingSpaceDescriptionRepository = workingSpaceDescriptionRepository;
    }
}
