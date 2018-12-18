package com.gliesereum.karma.service.car.impl;

import com.gliesereum.karma.model.entity.car.CarEntity;
import com.gliesereum.karma.model.repository.jpa.car.CarRepository;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.car.CarServiceClassCarService;
import com.gliesereum.karma.service.car.ServiceClassCarService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.car.CarDto;
import com.gliesereum.share.common.model.dto.karma.car.CarServiceClassCarDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.CAR_NOT_FOUND;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.SERVICE_CLASS_NOT_FOUND;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/5/18
 */
@Slf4j
@Service
public class CarServiceImpl extends DefaultServiceImpl<CarDto, CarEntity> implements CarService {

    private static final Class<CarDto> DTO_CLASS = CarDto.class;
    private static final Class<CarEntity> ENTITY_CLASS = CarEntity.class;

    @Autowired
    private CarRepository repository;

    @Autowired
    private CarServiceClassCarService carServiceClassCarService;

    @Autowired
    private ServiceClassCarService serviceClassCarService;

    public CarServiceImpl(CarRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<CarDto> getAllByUserId(UUID userId) {
        List<CarEntity> entities = repository.getAllByUserId(userId);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public void deleteByUserId(UUID id) {
        repository.deleteAllByUserId(id);
    }

    @Override
    public CarDto create(CarDto dto) {
        if (dto != null) {
            UUID userId = SecurityUtil.getUserId();
            if (userId == null) {
                throw new ClientException(USER_IS_ANONYMOUS);
            }
            dto.setUserId(userId);
            dto = super.create(dto);
        }
        return dto;
    }

    @Override
    public CarDto update(CarDto dto) {
        if (dto != null) {
            if (dto.getId() == null) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            UUID userId = SecurityUtil.getUserId();
            if (userId == null) {
                throw new ClientException(USER_IS_ANONYMOUS);
            }
            checkCarExist(dto.getId(), userId);
            dto.setUserId(userId);
            dto = super.update(dto);

        }
        return dto;
    }

    @Override
    @Transactional
    public CarDto addService(UUID idCar, UUID idService) {
        checkCarExist(idCar);
        checkServiceExist(idService);
        carServiceClassCarService.create(new CarServiceClassCarDto(idCar, idService));
        return getById(idCar);
    }

    @Override
    public CarDto removeService(UUID idCar, UUID idService) {
        checkCarExist(idCar);
        carServiceClassCarService.deleteByIdCarAndIdService(idCar, idService);
        return getById(idCar);
    }

    @Override
    public void checkCarExist(UUID id) {
        if (!repository.existsByIdAndUserId(id, SecurityUtil.getUserId())) {
            throw new ClientException(CAR_NOT_FOUND);
        }
    }

    public void checkCarExist(UUID id, UUID userId) {
        if (!repository.existsByIdAndUserId(id, userId)) {
            throw new ClientException(CAR_NOT_FOUND);
        }
    }

    private void checkServiceExist(UUID id) {
        if (!serviceClassCarService.existsService(id)) {
            throw new ClientException(SERVICE_CLASS_NOT_FOUND);
        }
    }
}
