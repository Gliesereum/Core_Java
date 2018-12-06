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


    @Autowired
    private CarRepository repository;

    @Autowired
    private CarServiceClassCarService carServiceClassCarService;

    @Autowired
    private ServiceClassCarService serviceClassCarService;

    private static final Class<CarDto> DTO_CLASS = CarDto.class;
    private static final Class<CarEntity> ENTITY_CLASS = CarEntity.class;

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
    @Transactional
    public CarDto addService(UUID idCar, UUID idService) {
        checkCarExist(idCar);
        checkServiceExist(idService);
        carServiceClassCarService.create(new CarServiceClassCarDto(idCar,idService));
        return getById(idCar);
    }

    @Override
    public CarDto removeService(UUID idCar, UUID idService) {
        checkCarExist(idCar);
        carServiceClassCarService.deleteByIdCarAndIdService(idCar,idService);
        return getById(idCar);
    }

    private void checkCarExist(UUID id){
        if(!repository.existsCarEntityByUserIdAndId(SecurityUtil.getUserId(), id)){
            throw new ClientException(CAR_NOT_FOUND);
        }
    }

    private void checkServiceExist(UUID id){
        if(!serviceClassCarService.existsService(id)){
            throw new ClientException(SERVICE_CLASS_NOT_FOUND);
        }
    }
}
