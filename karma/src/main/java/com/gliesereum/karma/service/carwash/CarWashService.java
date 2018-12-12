package com.gliesereum.karma.service.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashEntity;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface CarWashService extends DefaultService<CarWashDto, CarWashEntity> {

    boolean existByIdAndUserBusinessId(UUID id, UUID userBusinessId);

    boolean currentUserHavePermissionToAction(UUID carWashId);
}
