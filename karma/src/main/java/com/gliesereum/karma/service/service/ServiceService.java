package com.gliesereum.karma.service.service;

import com.gliesereum.karma.model.entity.service.ServiceEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.service.ServiceDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */
public interface ServiceService extends DefaultService<ServiceDto, ServiceEntity> {

    List<ServiceDto> getAllByServiceType(ServiceType type);
}
