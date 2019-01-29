package com.gliesereum.karma.service.filter;

import com.gliesereum.karma.model.entity.filter.FilterEntity;
import com.gliesereum.share.common.model.dto.karma.filter.FilterDto;
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
