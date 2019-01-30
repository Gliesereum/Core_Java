package com.gliesereum.karma.service.car;

import com.gliesereum.karma.model.entity.car.CarFilterAttributeEntity;
import com.gliesereum.share.common.model.dto.karma.car.CarFilterAttributeDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface CarFilterAttributeService extends DefaultService<CarFilterAttributeDto, CarFilterAttributeEntity> {

    void deleteByCarIdAndFilterId(UUID idCar, UUID filterId);

    boolean existsByCarIdAndFilterAttributeId(UUID idCar, UUID filterAttributeId);

}
