package com.gliesereum.karma.service.record;

import com.gliesereum.karma.model.entity.record.RecordServiceEntity;
import com.gliesereum.share.common.model.dto.karma.record.RecordServiceDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface RecordServiceService extends DefaultService<RecordServiceDto, RecordServiceEntity> {

    Map<UUID, List<ServicePriceDto>> getServicePriceMap(List<UUID> recordIds);
}
