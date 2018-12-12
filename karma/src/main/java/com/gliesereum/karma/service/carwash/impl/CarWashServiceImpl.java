package com.gliesereum.karma.service.carwash.impl;

import com.gliesereum.karma.model.entity.carwash.CarWashEntity;
import com.gliesereum.karma.model.repository.jpa.carwash.CarWashRepository;
import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    private final CarWashRepository carWashRepository;

    public CarWashServiceImpl(CarWashRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.carWashRepository = repository;
    }

    @Override
    public boolean existByIdAndUserBusinessId(UUID id, UUID userBusinessId) {
        return carWashRepository.existsByIdAndUserBusinessId(id, userBusinessId);
    }

    @Override
    public boolean currentUserHavePermissionToAction(UUID carWashId) {
        boolean result = false;
        UUID userBusinessId = SecurityUtil.getUserBusinessId();
        if (userBusinessId != null) {
            result = existByIdAndUserBusinessId(carWashId, userBusinessId);
        }
        return result;
    }
}
