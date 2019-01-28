package com.gliesereum.karma.service.car.impl;

import com.gliesereum.karma.model.entity.car.CarFilterAttributeEntity;
import com.gliesereum.karma.model.repository.jpa.car.CarFilterAttributeRepository;
import com.gliesereum.karma.service.car.CarFilterAttributeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.car.CarFilterAttributeDto;
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
public class CarFilterAttributeServiceImpl extends DefaultServiceImpl<CarFilterAttributeDto, CarFilterAttributeEntity> implements CarFilterAttributeService {

    @Autowired
    private CarFilterAttributeRepository repository;

    private static final Class<CarFilterAttributeDto> DTO_CLASS = CarFilterAttributeDto.class;
    private static final Class<CarFilterAttributeEntity> ENTITY_CLASS = CarFilterAttributeEntity.class;

    public CarFilterAttributeServiceImpl(CarFilterAttributeRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    @Transactional
    public void deleteByCarIdAndFilterId(UUID idCar, UUID filterAttributeId) {
        repository.deleteByCarIdAndFilterAttributeId(idCar, filterAttributeId);
    }

}
