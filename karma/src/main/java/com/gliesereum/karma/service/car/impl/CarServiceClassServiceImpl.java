package com.gliesereum.karma.service.car.impl;

import com.gliesereum.karma.model.entity.car.CarServiceClassEntity;
import com.gliesereum.karma.model.repository.jpa.car.CarServiceClassRepository;
import com.gliesereum.karma.service.car.CarServiceClassService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.car.CarServiceClassDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class CarServiceClassServiceImpl extends DefaultServiceImpl<CarServiceClassDto, CarServiceClassEntity> implements CarServiceClassService {

    @Autowired
    private CarServiceClassRepository repository;

    private static final Class<CarServiceClassDto> DTO_CLASS = CarServiceClassDto.class;
    private static final Class<CarServiceClassEntity> ENTITY_CLASS = CarServiceClassEntity.class;

    public CarServiceClassServiceImpl(CarServiceClassRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    @Transactional
    public void deleteByIdCarAndIdService(UUID idCar, UUID idService) {
       repository.deleteByCarIdAndAndServiceClassId(idCar, idService);
    }
}
