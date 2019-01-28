package com.gliesereum.karma.service.common;

import com.gliesereum.karma.model.entity.common.ServiceClassEntity;
import com.gliesereum.share.common.model.dto.karma.common.ServiceClassDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
public interface ServiceClassService extends DefaultService<ServiceClassDto, ServiceClassEntity> {

    boolean existsService(UUID id);

    List<ServiceClassDto> getAllByServiceType(ServiceType serviceType);
}
