package com.gliesereum.karma.service.car;

import com.gliesereum.karma.model.entity.car.CarServiceClassCarEntity;
import com.gliesereum.share.common.model.dto.karma.car.CarServiceClassCarDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
public interface CarServiceClassCarService extends DefaultService<CarServiceClassCarDto, CarServiceClassCarEntity> {

    void deleteByIdCarAndIdService(UUID idCar, UUID idService);
}
