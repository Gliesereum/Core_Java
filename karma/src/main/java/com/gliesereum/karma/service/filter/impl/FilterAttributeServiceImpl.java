package com.gliesereum.karma.service.filter.impl;

import com.gliesereum.karma.model.entity.filter.FilterAttributeEntity;
import com.gliesereum.karma.model.repository.jpa.filter.FilterAttributeRepository;
import com.gliesereum.karma.service.filter.FilterAttributeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.filter.FilterAttributeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.FILTER_ATTRIBUTE_NOT_FOUND;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class FilterAttributeServiceImpl extends DefaultServiceImpl<FilterAttributeDto, FilterAttributeEntity> implements FilterAttributeService {

    private static final Class<FilterAttributeDto> DTO_CLASS = FilterAttributeDto.class;
    private static final Class<FilterAttributeEntity> ENTITY_CLASS = FilterAttributeEntity.class;

    private final FilterAttributeRepository filterAttributeRepository;

    @Autowired
    public FilterAttributeServiceImpl(FilterAttributeRepository filterAttributeRepository, DefaultConverter defaultConverter) {
        super(filterAttributeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.filterAttributeRepository = filterAttributeRepository;
    }

    @Override
    public List<FilterAttributeDto> getByFilterId(UUID filterId) {
        List<FilterAttributeEntity> entities = filterAttributeRepository.findAllByFilterId(filterId);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public void checkFilterAttributeExist(UUID id) {
        if (!repository.existsById(id)) {
            throw new ClientException(FILTER_ATTRIBUTE_NOT_FOUND);
        }
    }

    @Override
    public List<UUID> checkFilterAttributeExistAndGetAllIdsByFilterId(UUID filterAttributeId) {
        if (filterAttributeId == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        Optional<FilterAttributeEntity> byId = filterAttributeRepository.findById(filterAttributeId);
        FilterAttributeEntity filterAttribute = byId.orElseThrow(() -> new ClientException(FILTER_ATTRIBUTE_NOT_FOUND));
        UUID filterId = filterAttribute.getFilterId();
        List<UUID> result = null;
        List<FilterAttributeDto> byFilterId = getByFilterId(filterId);
        if (CollectionUtils.isNotEmpty(byFilterId)) {
            result = byFilterId.stream().map(FilterAttributeDto::getId).collect(Collectors.toList());
        }
        return result;
    }
}
