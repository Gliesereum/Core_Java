package com.gliesereum.karma.service.service;

import com.gliesereum.karma.model.entity.service.ServicePriceEntity;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
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

    void addFilterAttribute(UUID idPrice, UUID idAttribute);

    ServicePriceDto addFilterAttributes(UUID idPrice, List<UUID> idsAttribute);

    void removeFilterAttribute(UUID idPrice, UUID idAttribute);

    ServicePriceDto getByIdAndRefresh(UUID id);
}
