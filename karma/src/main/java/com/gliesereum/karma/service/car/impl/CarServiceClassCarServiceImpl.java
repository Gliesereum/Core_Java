package com.gliesereum.karma.service.car.impl;

import com.gliesereum.karma.model.entity.car.CarServiceClassCarEntity;
import com.gliesereum.karma.model.repository.jpa.car.CarServiceClassCarRepository;
import com.gliesereum.karma.service.car.CarServiceClassCarService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.car.CarServiceClassCarDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Slf4j
@Service
public class CarServiceClassCarServiceImpl extends DefaultServiceImpl<CarServiceClassCarDto, CarServiceClassCarEntity> implements CarServiceClassCarService {

    @Autowired
    private CarServiceClassCarRepository repository;

    private static final Class<CarServiceClassCarDto> DTO_CLASS = CarServiceClassCarDto.class;
    private static final Class<CarServiceClassCarEntity> ENTITY_CLASS = CarServiceClassCarEntity.class;

    public CarServiceClassCarServiceImpl(CarServiceClassCarRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    @Transactional
    public void deleteByIdCarAndIdService(UUID idCar, UUID idService) {
       repository.deleteByCarIdAndAndServiceClassId(idCar, idService);
    }
}
