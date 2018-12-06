package com.gliesereum.karma.service.car;

import com.gliesereum.karma.model.entity.car.ServiceClassCarEntity;
import com.gliesereum.share.common.model.dto.karma.car.ServiceClassCarDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
public interface ServiceClassCarService extends DefaultService<ServiceClassCarDto, ServiceClassCarEntity> {

    boolean existsService(UUID id);
}
