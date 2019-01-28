package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.ServicePriceEntity;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ServicePriceService extends DefaultService<ServicePriceDto, ServicePriceEntity> {

    List<ServicePriceDto> getAllByPackage(UUID id);

    List<ServicePriceDto> getByBusinessId(UUID id);

    ServicePriceDto addFilterAttribute(UUID idPrice, UUID idAttribute);

    ServicePriceDto removeFilterAttribute(UUID idPrice, UUID idAttribute);
}
