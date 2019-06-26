package com.gliesereum.karma.service.record.impl;

import com.gliesereum.karma.model.entity.record.RecordMessageEntity;
import com.gliesereum.karma.model.repository.jpa.record.RecordMessageRepository;
import com.gliesereum.karma.service.record.RecordMessageDescriptionService;
import com.gliesereum.karma.service.record.RecordMessageService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordMessageDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordMessageDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class RecordMessageServiceImpl extends DefaultServiceImpl<RecordMessageDto, RecordMessageEntity> implements RecordMessageService {

    private static final Class<RecordMessageDto> DTO_CLASS = RecordMessageDto.class;
    private static final Class<RecordMessageEntity> ENTITY_CLASS = RecordMessageEntity.class;

    public RecordMessageServiceImpl(RecordMessageRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.repository = repository;
    }

    private final RecordMessageRepository repository;

    @Autowired
    private RecordMessageDescriptionService recordMessageDescriptionService;

    @Override
    @Transactional
    public RecordMessageDto create(RecordMessageDto dto) {
        RecordMessageDto result = null;
        if (dto != null) {
            result = super.create(dto);
            DescriptionReadableDto<RecordMessageDescriptionDto> descriptions = recordMessageDescriptionService.create(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
        }
        return result;
    }

    @Override
    @Transactional
    public RecordMessageDto update(RecordMessageDto dto) {
        RecordMessageDto result = null;
        if (dto != null) {
            result = super.update(dto);
            DescriptionReadableDto<RecordMessageDescriptionDto> descriptions = recordMessageDescriptionService.update(dto.getDescriptions(), result.getId());
            result.setDescriptions(descriptions);
        }
        return result;
    }

}
