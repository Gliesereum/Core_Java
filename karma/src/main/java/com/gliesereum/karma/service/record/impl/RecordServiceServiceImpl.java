package com.gliesereum.karma.service.record.impl;

import com.gliesereum.karma.model.entity.record.RecordServiceEntity;
import com.gliesereum.karma.model.repository.jpa.record.RecordServiceRepository;
import com.gliesereum.karma.service.record.RecordServiceService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.record.RecordServiceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
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

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    public RecordServiceServiceImpl(RecordServiceRepository recordServiceRepository, DefaultConverter defaultConverter) {
        super(recordServiceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.recordServiceRepository = recordServiceRepository;
    }

    @Override
    public Map<UUID, List<UUID>> getServicePriceIds(List<UUID> recordIds) {
        Map<UUID, List<UUID>> result = null;
        if (CollectionUtils.isNotEmpty(recordIds)) {
            List<RecordServiceEntity> recordServices = recordServiceRepository.findAllByRecordIdIn(recordIds);
            if (CollectionUtils.isNotEmpty(recordServices)) {
                result = recordServices.stream()
                        .collect(Collectors.groupingBy(
                                RecordServiceEntity::getRecordId,
                                Collectors.mapping(RecordServiceEntity::getServiceId, Collectors.toList())));
            }
        }
        return result;
    }

    @Override
    public Map<UUID, List<ServicePriceDto>> getServicePriceMap(List<UUID> recordIds) {
        Map<UUID, List<ServicePriceDto>> result = null;
        if (CollectionUtils.isNotEmpty(recordIds)) {
            Map<UUID, List<UUID>> servicePriceIds = getServicePriceIds(recordIds);
            if (MapUtils.isNotEmpty(servicePriceIds)) {
                Set<UUID> serviceIds = servicePriceIds.entrySet().stream()
                        .flatMap(i -> i.getValue().stream())
                        .collect(Collectors.toSet());
                List<ServicePriceDto> servicePrices = servicePriceService.getByIds(serviceIds);
                if (CollectionUtils.isNotEmpty(servicePrices)) {
                    Map<UUID, ServicePriceDto> servicePriceMap = servicePrices.stream()
                            .collect(Collectors.toMap(ServicePriceDto::getId, i -> i));
                    result = new HashMap<>();
                    for (Map.Entry<UUID, List<UUID>> servicePriceId : servicePriceIds.entrySet()) {
                        List<ServicePriceDto> prices = servicePriceId.getValue().stream()
                                .map(servicePriceMap::get)
                                .collect(Collectors.toList());
                        result.put(servicePriceId.getKey(), prices);
                    }
                }
            }
        }
        return result;
    }
}
