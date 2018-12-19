package com.gliesereum.karma.service.carwash.impl;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordEntity;
import com.gliesereum.karma.model.repository.jpa.carwash.CarWashRecordRepository;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.carwash.CarWashRecordService;
import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.karma.service.common.OrderService;
import com.gliesereum.karma.service.common.PackageService;
import com.gliesereum.karma.service.common.WorkingSpaceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.car.CarDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordDto;
import com.gliesereum.share.common.model.dto.karma.common.OrderDto;
import com.gliesereum.share.common.model.dto.karma.common.PackageDto;
import com.gliesereum.share.common.model.dto.karma.common.ServicePriceDto;
import com.gliesereum.share.common.model.dto.karma.common.WorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusWashing;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

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

    @Autowired
    private WorkingSpaceService workingSpaceService;

    @Autowired
    private OrderService orderService;

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
            carService.checkCarExistInCurrentUser(carId);
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
        LocalTime beginTime = LocalTime.now();
        if (StringUtils.isNotEmpty(params.get("beginTime"))) {
            beginTime = LocalTime.parse(params.get("beginTime"), DateTimeFormatter.ofPattern("HH:mm"));
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
        List<UUID> workingSpaceIds = carWash.getSpaces().stream().map(WorkingSpaceDto::getId).collect(Collectors.toList());
        List<CarWashRecordEntity> entities = repository.findByStatusRecordAndDateAndWorkingSpaceIdIn(status, date, workingSpaceIds);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public CarWashRecordDto updateTimeRecord(UUID idRecord, String beginTime, Boolean isUser) {
        LocalTime startTime = LocalTime.parse(beginTime, DateTimeFormatter.ofPattern("HH:mm"));
        //todo check time
        return null;
    }

    @Override
    @Transactional
    public CarWashRecordDto updateWashingSpace(UUID idRecord, UUID workingSpaceId, Boolean isUser) {
        CarWashRecordDto dto = getById(idRecord);
        checkPermissionToUpdate(dto, isUser);
        WorkingSpaceDto workingSpace = workingSpaceService.getById(workingSpaceId);
        if (workingSpace == null) {
            throw new ClientException(WORKING_SPACE_NOT_FOUND);
        }
        checkWorkingSpaceByOpportunityRecordToTime(dto.getDate(), dto.getBeginTime(), dto.getFinishTime(), workingSpaceId);
        dto.setWorkingSpaceId(workingSpaceId);
        return super.update(dto);
    }

    @Override
    @Transactional
    public CarWashRecordDto updateStatusWashing(UUID idRecord, StatusWashing status, Boolean isUser) {
        CarWashRecordDto dto = getById(idRecord);
        checkPermissionToUpdate(dto, isUser);
        dto.setStatusWashing(status);
        return super.update(dto);
    }

    @Override
    @Transactional
    public CarWashRecordDto updateStatusRecord(UUID idRecord, StatusRecord status, Boolean isUser) {
        CarWashRecordDto dto = getById(idRecord);
        checkPermissionToUpdate(dto, isUser);
        dto.setStatusRecord(status);
        return super.update(dto);
    }

    @Override
    @Transactional
    public CarWashRecordDto create(CarWashRecordDto dto) {
        if (dto != null) {
            if (dto.getBeginTime() == null) {
                throw new ClientException(TIME_BEGIN_EMPTY);
            }
            dto.setFinishTime(dto.getBeginTime().plusMinutes(getDurationByRecord(dto)));
            dto.setPrice(getPriceByRecord(dto));
            checkRecord(dto);
            CarWashRecordDto result = super.create(dto);
            if (result != null) {
                createOrders(result);
            }
            return result;
        }
        return null;
    }

    private void checkRecord(CarWashRecordDto dto) {
        if (dto != null) {
            if (dto.getPackageId() == null && CollectionUtils.isEmpty(dto.getServices())) {
                throw new ClientException(SERVICE_NOT_CHOOSE);
            }
            if (dto.getCarWashId() == null) {
                throw new ClientException(CARWASH_ID_EMPTY);
            }
            CarWashDto carWash = carWashService.getById(dto.getCarWashId());
            if (carWash == null) {
                throw new ClientException(CARWASH_NOT_FOUND);
            }
            if (dto.getWorkingSpaceId() == null) {
                throw new ClientException(WORKING_SPACE_ID_EMPTY);
            }
            WorkingSpaceDto workingSpace = carWash.getSpaces().stream().filter(ws -> ws.getId().equals(dto.getWorkingSpaceId())).findFirst().orElse(null);
            if (workingSpace == null) {
                throw new ClientException(WORKING_SPACE_NOT_FOUND_IN_THIS_CARWASH);
            }
            checkWorkingSpaceByOpportunityRecordToTime(dto.getDate(), dto.getBeginTime(), dto.getFinishTime(), dto.getWorkingSpaceId());
        }
    }

    private void checkWorkingSpaceByOpportunityRecordToTime(LocalDate date, LocalTime start, LocalTime finish, UUID workingSpaceId) {
        List<CarWashRecordEntity> records = repository.findByDateAndStatusRecordAndWorkingSpaceId(date, StatusRecord.CREATED, workingSpaceId);
        if (CollectionUtils.isNotEmpty(records)) {

            //todo check time
        }
    }

    private Integer getPriceByRecord(CarWashRecordDto dto) {
        int result = 0;
        if (dto != null && dto.getPackageId() != null) {
            PackageDto packageDto = packageService.getById(dto.getPackageId());
            if (packageDto != null && CollectionUtils.isNotEmpty(packageDto.getServices())) {
                int sumByPackage = packageDto.getServices().stream().mapToInt(ServicePriceDto::getPrice).sum();
                if (packageDto.getDiscount() > 0) {
                    result += (sumByPackage - ((sumByPackage / 100) * packageDto.getDiscount()));
                } else {
                    result += sumByPackage;
                }
            } else throw new ClientException(PACKAGE_NOT_FOUND);

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
            } else throw new ClientException(PACKAGE_NOT_FOUND);
        }
        return result;
    }

    private void checkPermissionToUpdate(CarWashRecordDto dto, Boolean isUser) {
        if (dto == null) {
            throw new ClientException(RECORD_NOT_FOUND);
        }
        if (!isUser && !carWashService.currentUserHavePermissionToAction(dto.getCarWashId())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_RECORD);
        }
        if (isUser) {
            CarDto car = carService.getById(dto.getCarId());
            if (car != null && !car.getUserId().equals(SecurityUtil.getUserId())) {
                throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_RECORD);
            }
        }
    }

    private void createOrders(CarWashRecordDto dto) {
        if (dto != null && CollectionUtils.isNotEmpty(dto.getServices())) {
            dto.getServices().forEach(f -> {
                orderService.create(new OrderDto(f.getServiceId(), dto.getId(), f.getPrice(), false));
            });
        }
        if (dto != null && dto.getPackageId() != null) {
            PackageDto packageDto = packageService.getById(dto.getPackageId());
            if (packageDto != null) {
                packageDto.getServices().forEach(f -> {
                    int price = f.getPrice() - ((f.getPrice() / 100) * packageDto.getDiscount());
                    orderService.create(new OrderDto(f.getServiceId(), dto.getId(), price, true));
                });
            } else throw new ClientException(PACKAGE_NOT_FOUND);
        }
    }

}
