package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.ServiceClassPriceEntity;
import com.gliesereum.share.common.model.dto.karma.common.ServiceClassPriceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface ServiceClassPriceService extends DefaultService<ServiceClassPriceDto, ServiceClassPriceEntity> {

    List<ServiceClassPriceDto> getByPriceId(UUID priceId);

    void delete(UUID priceId, UUID classId);
}
