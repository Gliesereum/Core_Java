package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.WorkTimeEntity;
import com.gliesereum.karma.model.repository.jpa.common.WorkTimeRepository;
import com.gliesereum.karma.service.common.WorkTimeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.WorkTimeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public WorkTimeServiceImpl(WorkTimeRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

}
