package com.gliesereum.karma.service.car;

import com.gliesereum.karma.model.entity.car.CarEntity;
import com.gliesereum.share.common.model.dto.karma.car.CarDto;
import com.gliesereum.share.common.model.dto.karma.car.CarInfoDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
public interface CarService extends DefaultService<CarDto, CarEntity> {

    List<CarDto> getAllByUserId(UUID userId);

    void deleteByUserId(UUID id);

    void checkCarExistInCurrentUser(UUID id);

    CarDto addService(UUID idCar, UUID idService);

    CarDto removeService(UUID idCar, UUID idService);

    CarInfoDto getCarInfo(UUID idCar);

}
