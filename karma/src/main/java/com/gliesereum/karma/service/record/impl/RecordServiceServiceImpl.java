package com.gliesereum.karma.service.record.impl;

import com.gliesereum.karma.model.entity.record.RecordServiceEntity;
import com.gliesereum.karma.model.repository.jpa.record.RecordServiceRepository;
import com.gliesereum.karma.service.record.RecordServiceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.record.RecordServiceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

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

    private final RecordServiceRepository recordServiceRepository;

    public RecordServiceServiceImpl(RecordServiceRepository recordServiceRepository, DefaultConverter defaultConverter) {
        super(recordServiceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.recordServiceRepository = recordServiceRepository;
    }

    @Override
    public Map<UUID, List<ServicePriceDto>> getServicePriceMap(List<UUID> recordIds) {
        Map<UUID, List<ServicePriceDto>> result = null;
        if (CollectionUtils.isNotEmpty(recordIds)) {
            List<RecordServiceEntity> entities = recordServiceRepository.findAllByRecordIdIn(recordIds);
            if (CollectionUtils.isNotEmpty(entities)) {
                result = new HashMap<>();
                for (RecordServiceEntity entity : entities) {
                    if (!result.containsKey(entity.getRecordId())) {
                        result.put(entity.getRecordId(), new ArrayList<>());
                    }
                    result.get(entity.getRecordId()).add(converter.convert(entity, dtoClass).getService());
                }
            }
        }
        return result;
    }
}
