package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.FilterEntity;
import com.gliesereum.share.common.model.dto.karma.common.FilterDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
public interface FilterService extends DefaultService<FilterDto, FilterEntity> {

    List<FilterDto> getAllByServiceType(ServiceType serviceType);
}
