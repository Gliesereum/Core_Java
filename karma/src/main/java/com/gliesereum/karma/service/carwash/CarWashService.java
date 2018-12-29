package com.gliesereum.karma.service.carwash;

import com.gliesereum.karma.model.entity.carwash.CarWashEntity;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashFullModel;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
public interface CarWashService extends DefaultService<CarWashDto, CarWashEntity> {

    boolean existByIdAndUserBusinessIds(UUID id, List<UUID> userBusinessIds);

    boolean currentUserHavePermissionToAction(UUID carWashId);

    List<CarWashDto> getByUserBusinessIds(List<UUID> businessId);

    CarWashFullModel getFullModelById(UUID id);

    List<CarWashDto> getByBusinessId(UUID businessId);
}
