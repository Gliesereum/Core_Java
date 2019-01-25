package com.gliesereum.karma.service.carwash.impl;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordServiceEntity;
import com.gliesereum.karma.model.repository.jpa.carwash.CarWashRecordServiceRepository;
import com.gliesereum.karma.service.carwash.CarWashRecordServiceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordServiceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class CarWashRecordServiceServiceImpl extends DefaultServiceImpl<CarWashRecordServiceDto, CarWashRecordServiceEntity> implements CarWashRecordServiceService {

    private static final Class<CarWashRecordServiceDto> DTO_CLASS = CarWashRecordServiceDto.class;
    private static final Class<CarWashRecordServiceEntity> ENTITY_CLASS = CarWashRecordServiceEntity.class;

    public CarWashRecordServiceServiceImpl(CarWashRecordServiceRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

}
