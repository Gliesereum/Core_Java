package com.gliesereum.karma.service.service;

import com.gliesereum.karma.model.entity.service.ServiceClassPriceEntity;
import com.gliesereum.share.common.model.dto.karma.service.ServiceClassPriceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ServiceClassPriceService extends DefaultService<ServiceClassPriceDto, ServiceClassPriceEntity> {

    List<ServiceClassPriceDto> getByPriceId(UUID priceId);

    void delete(UUID priceId, UUID classId);

    ServicePriceDto createAndGetPrice(ServiceClassPriceDto dto);

    ServicePriceDto updateAndGetPrice(ServiceClassPriceDto dto);
}
