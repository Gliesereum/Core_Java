package com.gliesereum.karma.service.carwash.impl;

import com.gliesereum.karma.model.entity.carwash.CarWashEntity;
import com.gliesereum.karma.model.repository.jpa.carwash.CarWashRepository;
import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Slf4j
@Service
public class CarWashServiceImpl extends DefaultServiceImpl<CarWashDto, CarWashEntity> implements CarWashService {

    private static final Class<CarWashDto> DTO_CLASS = CarWashDto.class;
    private static final Class<CarWashEntity> ENTITY_CLASS = CarWashEntity.class;

    public CarWashServiceImpl(CarWashRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

}
