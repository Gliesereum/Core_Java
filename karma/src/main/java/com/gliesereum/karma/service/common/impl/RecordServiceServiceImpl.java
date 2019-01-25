package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.RecordServiceEntity;
import com.gliesereum.karma.model.repository.jpa.common.RecordServiceRepository;
import com.gliesereum.karma.service.common.RecordServiceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.common.RecordServiceDto;
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
public class RecordServiceServiceImpl extends DefaultServiceImpl<RecordServiceDto, RecordServiceEntity> implements RecordServiceService {

    private static final Class<RecordServiceDto> DTO_CLASS = RecordServiceDto.class;
    private static final Class<RecordServiceEntity> ENTITY_CLASS = RecordServiceEntity.class;

    public RecordServiceServiceImpl(RecordServiceRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

}
