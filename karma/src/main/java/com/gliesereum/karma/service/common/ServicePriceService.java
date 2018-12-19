package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.ServicePriceEntity;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface ServicePriceService extends DefaultService<ServicePriceDto, ServicePriceEntity> {

    List<ServicePriceDto> getAllByPackage(UUID id);

    List<ServicePriceDto> getByBusinessServiceId(UUID id);
}
