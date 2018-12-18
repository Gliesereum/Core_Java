package com.gliesereum.karma.service.carwash.impl;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordEntity;
import com.gliesereum.karma.model.repository.jpa.carwash.CarWashRecordRepository;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.carwash.CarWashRecordService;
import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.karma.service.common.PackageService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.car.CarDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordDto;
import com.gliesereum.share.common.model.dto.karma.common.PackageDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.common.WorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.CAR_NOT_FOUND;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Slf4j
@Service
public class CarWashRecordServiceImpl extends DefaultServiceImpl<CarWashRecordDto, CarWashRecordEntity> implements CarWashRecordService {

    @Autowired
    private CarWashRecordRepository repository;

    @Autowired
    private CarService carService;

    @Autowired
    private CarWashService carWashService;

    @Autowired
    private PackageService packageService;

    private static final Class<CarWashRecordDto> DTO_CLASS = CarWashRecordDto.class;
    private static final Class<CarWashRecordEntity> ENTITY_CLASS = CarWashRecordEntity.class;

    public CarWashRecordServiceImpl(CarWashRecordRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<CarWashRecordDto> getByParamsForClient(Map<String, String> params) {
        List<UUID> listCarId = new ArrayList<>();
        if (StringUtils.isNotEmpty(params.get("carId"))) {
            UUID carId = UUID.fromString(params.get("carId"));
            carService.checkCarExist(carId);
            listCarId.add(carId);
        } else {
            List<CarDto> carsUsers = carService.getAllByUserId(SecurityUtil.getUserId());
            if (CollectionUtils.isNotEmpty(carsUsers)) {
                listCarId = carsUsers.stream().map(CarDto::getId).collect(Collectors.toList());
            } else {
                throw new ClientException(CAR_NOT_FOUND);
            }
        }
        StatusRecord status = StatusRecord.CREATED;
        if (StringUtils.isNotEmpty(params.get("status"))) {
            status = StatusRecord.valueOf(params.get("status"));
        }
        LocalDate date = LocalDate.now();
        if (StringUtils.isNotEmpty(params.get("date"))) {
            date = LocalDate.parse(params.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        Time beginTime = Time.valueOf(LocalTime.now());
        if (StringUtils.isNotEmpty(params.get("beginTime"))) {
            beginTime = Time.valueOf(params.get("beginTime"));
        }
        List<CarWashRecordEntity> entities =
                repository.findByBeginTimeGreaterThanEqualAndStatusRecordAndDateAndCarIdIn(beginTime, status, date, listCarId);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public List<CarWashRecordDto> getByParamsForBusiness(Map<String, String> params) {
        UUID carWashId = UUID.fromString(params.get("carWashId"));
        CarWashDto carWash = carWashService.getById(carWashId);
        if (carWash == null || CollectionUtils.isEmpty(carWash.getSpaces())) {
            return Collections.EMPTY_LIST;
        }
        LocalDate date = LocalDate.now();
        if (StringUtils.isNotEmpty(params.get("date"))) {
            date = LocalDate.parse(params.get("date"), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
        StatusRecord status = StatusRecord.CREATED;
        if (StringUtils.isNotEmpty(params.get("status"))) {
            status = StatusRecord.valueOf(params.get("status"));
        }
        List<UUID> placeIds = carWash.getSpaces().stream().map(WorkingSpaceDto::getId).collect(Collectors.toList());
        List<CarWashRecordEntity> entities = repository.findByStatusRecordAndDateAndPlaceIdIn(status, date, placeIds);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public CarWashRecordDto create(CarWashRecordDto dto) {
        if (dto != null) {
            dto.setPrice(getPriceByRecord(dto));
            return super.create(dto);
        }
        return null;
    }

    @Override
    @Transactional
    public CarWashRecordDto update(CarWashRecordDto dto) {
        if (dto != null) {
            dto.setPrice(getPriceByRecord(dto));

            return super.update(dto);
        }
        return null;
    }

    private boolean checkOpportunityRecord(CarWashRecordDto dto) {

        return false;
    }

    private Integer getPriceByRecord(CarWashRecordDto dto) {
        int result = 0;
        if (dto != null && dto.getPackageId() != null) {
            PackageDto packageDto = packageService.getById(dto.getPackageId());
            if (packageDto != null && CollectionUtils.isNotEmpty(packageDto.getServices())) {
                int sumByPackage = packageDto.getServices().stream().mapToInt(ServicePriceDto::getPrice).sum();
                if (packageDto.getDiscount() > 0) {
                    result += (sumByPackage - (sumByPackage / packageDto.getDiscount()));
                } else {
                    result += sumByPackage;
                }
            }
        }
        if (dto != null && CollectionUtils.isNotEmpty(dto.getServices())) {
            result += dto.getServices().stream().mapToInt(ServicePriceDto::getPrice).sum();
        }
        return result;
    }

    private Integer getDurationByRecord(CarWashRecordDto dto) {
        int result = 0;
        if (dto != null && CollectionUtils.isNotEmpty(dto.getServices())) {
            result += dto.getServices().stream().mapToInt(ServicePriceDto::getDuration).sum();
        }
        if (dto != null && dto.getPackageId() != null) {
            PackageDto packageDto = packageService.getById(dto.getPackageId());
            if (packageDto != null) {
                result += packageDto.getDuration();
            }
        }
        return result;
    }

}
