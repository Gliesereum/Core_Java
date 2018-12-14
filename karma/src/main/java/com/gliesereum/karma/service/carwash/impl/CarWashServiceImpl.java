package com.gliesereum.karma.service.carwash.impl;

import com.gliesereum.karma.model.entity.carwash.CarWashEntity;
import com.gliesereum.karma.model.repository.jpa.carwash.CarWashRepository;
import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.DONT_HAVE_PERMISSION_TO_CREATE_CARWASH;

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
    public CarWashDto create(CarWashDto dto) {
        if (dto != null) {
            UUID userBusinessId = SecurityUtil.getUserBusinessId();
            if (userBusinessId == null) {
                throw new ClientException(DONT_HAVE_PERMISSION_TO_CREATE_CARWASH);
            }
            dto.setUserBusinessId(userBusinessId);
            CarWashEntity entity = converter.convert(dto, entityClass);
            entity = repository.saveAndFlush(entity);
            dto = converter.convert(entity, dtoClass);
        }
        return dto;
    }

    @Override
    public CarWashDto update(CarWashDto dto) {
        if (dto != null) {
            if (dto.getId() == null) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            currentUserHavePermissionToAction(dto.getId());
            dto.setUserBusinessId(SecurityUtil.getUserBusinessId());
            CarWashEntity entity = converter.convert(dto, entityClass);
            entity = repository.saveAndFlush(entity);
            dto = converter.convert(entity, dtoClass);
        }
        return dto;
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

    @Override
    public List<CarWashDto> getByUserBusinessId(UUID userBusinessId) {
        List<CarWashDto> result = null;
        if (userBusinessId != null) {
            List<CarWashEntity> entities = carWashRepository.findByUserBusinessId(userBusinessId);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }
}
