package com.gliesereum.karma.service.carwash.impl;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordEntity;
import com.gliesereum.karma.model.repository.jpa.carwash.CarWashRecordRepository;
import com.gliesereum.karma.service.carwash.CarWashRecordService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Slf4j
@Service
public class CarWashRecordServiceImpl extends DefaultServiceImpl<CarWashRecordDto, CarWashRecordEntity> implements CarWashRecordService {

    @Autowired
    CarWashRecordRepository repository;

    private static final Class<CarWashRecordDto> DTO_CLASS = CarWashRecordDto.class;
    private static final Class<CarWashRecordEntity> ENTITY_CLASS = CarWashRecordEntity.class;

    public CarWashRecordServiceImpl(CarWashRecordRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<CarWashRecordDto> getByParamsForClient(Map<String, String> params) {

        List<CarWashRecordEntity> entities = repository.findAll();
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<CarWashRecordDto> getByParamsForBusiness(Map<String, String> params) {
        UUID businessId = SecurityUtil.getUserBusinessId();

        List<CarWashRecordEntity> entities = repository.findAll();
        return converter.convert(entities, dtoClass);
    }
}
