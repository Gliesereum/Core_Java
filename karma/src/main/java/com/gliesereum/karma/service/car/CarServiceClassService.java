package com.gliesereum.karma.service.car;

import com.gliesereum.karma.model.entity.car.CarServiceClassEntity;
import com.gliesereum.share.common.model.dto.karma.car.CarServiceClassDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CarServiceClassService extends DefaultService<CarServiceClassDto, CarServiceClassEntity> {

    void deleteByIdCarAndIdService(UUID idCar, UUID idService);
}
