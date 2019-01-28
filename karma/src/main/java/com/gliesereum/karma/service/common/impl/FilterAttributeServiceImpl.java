package com.gliesereum.karma.service.common.impl;

import com.gliesereum.karma.model.entity.common.FilterAttributeEntity;
import com.gliesereum.karma.model.repository.jpa.common.FilterAttributeRepository;
import com.gliesereum.karma.service.common.FilterAttributeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.common.FilterAttributeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.FILTER_ATTRIBUTE_NOT_FOUND;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class FilterAttributeServiceImpl extends DefaultServiceImpl<FilterAttributeDto, FilterAttributeEntity> implements FilterAttributeService {

    @Autowired
    private FilterAttributeRepository repository;

    private static final Class<FilterAttributeDto> DTO_CLASS = FilterAttributeDto.class;
    private static final Class<FilterAttributeEntity> ENTITY_CLASS = FilterAttributeEntity.class;

    public FilterAttributeServiceImpl(FilterAttributeRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<FilterAttributeDto> getByFilterId(UUID filterId) {
        List<FilterAttributeEntity> entities = repository.findAllByFilterId(filterId);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public void checkFilterAttributeExist(UUID id) {
        if (!repository.existsById(id)) {
            throw new ClientException(FILTER_ATTRIBUTE_NOT_FOUND);
        }
    }
}
