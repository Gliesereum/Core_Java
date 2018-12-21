package com.gliesereum.karma.service.carwash.impl;

import com.gliesereum.karma.model.entity.carwash.CarWashRecordEntity;
import com.gliesereum.karma.model.repository.jpa.carwash.CarWashRecordRepository;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.carwash.CarWashRecordService;
import com.gliesereum.karma.service.carwash.CarWashService;
import com.gliesereum.karma.service.common.OrderService;
import com.gliesereum.karma.service.common.PackageService;
import com.gliesereum.karma.service.common.ServicePriceService;
import com.gliesereum.karma.service.common.WorkingSpaceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.car.CarDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordDto;
import com.gliesereum.share.common.model.dto.karma.carwash.CarWashRecordFreeTimesModel;
import com.gliesereum.share.common.model.dto.karma.common.*;
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

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    @Autowired
    private ServicePriceService servicePriceService;

    private static final Class<CarWashRecordDto> DTO_CLASS = CarWashRecordDto.class;
    private static final Class<CarWashRecordEntity> ENTITY_CLASS = CarWashRecordEntity.class;

    public CarWashRecordServiceImpl(CarWashRecordRepository repository, DefaultConverter defaultConverter) {
        super(repository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }

    @Override
    public List<CarWashRecordDto> getByParamsForClient(Map<String, String> params) {
        return getByParams(params, false);
    }

    @Override
    public List<CarWashRecordDto> getByParamsForBusiness(Map<String, String> params) {
        return getByParams(params, true);
    }

    private List<CarWashRecordDto> getByParams(Map<String, String> params, boolean isBusiness) {

        StatusRecord status = StatusRecord.CREATED;
        if (StringUtils.isNotEmpty(params.get("status"))) {
            status = StatusRecord.valueOf(params.get("status"));
        }

        LocalDateTime from = LocalDateTime.now();
        if (StringUtils.isNotEmpty(params.get("from"))) {
            Long dateLong = Long.valueOf(params.get("from"));
            from = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong),
                    TimeZone.getDefault().toZoneId());
        }

        LocalDateTime to = from.toLocalDate().atTime(LocalTime.MAX);
        if (StringUtils.isNotEmpty(params.get("to"))) {
            Long dateLong = Long.valueOf(params.get("to"));
            to = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateLong),
                    TimeZone.getDefault().toZoneId());
        }

        List<CarWashRecordEntity> entities = null;

        if (isBusiness) {
            UUID carWashId = UUID.fromString(params.get("carWashId"));
            CarWashDto carWash = carWashService.getById(carWashId);
            if (carWash == null || CollectionUtils.isEmpty(carWash.getSpaces())) {
                List<CarWashRecordDto> emptyList = Collections.emptyList();
                return emptyList;
            }
            List<UUID> workingSpaceIds = carWash.getSpaces().stream().map(WorkingSpaceDto::getId).collect(Collectors.toList());
            entities = repository.findByStatusRecordAndWorkingSpaceIdInAndBeginBetweenOrderByBegin(status, workingSpaceIds, from, to);
        } else {
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
            entities = repository.findByStatusRecordAndCarIdInAndBeginBetween(status, listCarId, from, to);
        }
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public CarWashRecordDto updateTimeRecord(UUID idRecord, Long beginTime, Boolean isUser) {

        LocalDateTime begin = LocalDateTime.ofInstant(Instant.ofEpochMilli(beginTime),
                TimeZone.getDefault().toZoneId());

        CarWashRecordDto dto = getById(idRecord);
        if (dto == null) {
            throw new ClientException(RECORD_NOT_FOUND);
        }
        checkPermissionToUpdate(dto, isUser);
        dto.setBegin(begin);
        LocalDateTime finish = begin.plusMinutes(getDurationByRecord(dto));
        dto.setFinish(finish);
        checkRecord(dto);
        return super.update(dto);
    }

    @Override
    @Transactional
    public CarWashRecordDto updateWashingSpace(UUID idRecord, UUID workingSpaceId, Boolean isUser) {
        CarWashRecordDto dto = getById(idRecord);
        checkPermissionToUpdate(dto, isUser);
        CarWashDto carWash = getCarWashByRecord(dto);
        if (dto.getWorkingSpaceId() == null) {
            throw new ClientException(WORKING_SPACE_ID_EMPTY);
        }
        WorkingSpaceDto workingSpace = workingSpaceService.getById(workingSpaceId);
        if (workingSpace == null) {
            throw new ClientException(WORKING_SPACE_NOT_FOUND);
        }
        checkWorkingSpaceByOpportunityRecordToTime(dto.getBegin(), dto.getFinish(), workingSpaceId, carWash);
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
            if (dto.getBegin() == null) {
                throw new ClientException(TIME_BEGIN_EMPTY);
            }



            dto.setFinish(dto.getBegin().plusMinutes(getDurationByRecord(dto)));
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

    @Override
    public CarWashRecordFreeTimesModel getFreeTimeForRecord(CarWashRecordDto dto) {
        CarWashRecordFreeTimesModel result = new CarWashRecordFreeTimesModel();
        Map<LocalDateTime, UUID> times = new TreeMap<>();
        if (dto != null) {
            if (dto.getBegin() == null) {
                throw new ClientException(TIME_BEGIN_EMPTY);
            }
            CarWashDto carWash = getCarWashByRecord(dto);
            dto.setPrice(getPriceByRecord(dto));
            dto.setFinish(dto.getBegin().plusMinutes(getDurationByRecord(dto)));
            LocalDateTime begin = dto.getBegin();
            LocalDateTime finish = dto.getFinish();
            WorkTimeDto workTime = getWorkTimeByCarWash(begin, carWash);
            carWash.getSpaces().forEach(f -> {
                List<CarWashRecordEntity> records =
                        repository.findByStatusRecordAndWorkingSpaceIdInAndBeginBetweenOrderByBegin(
                                StatusRecord.CREATED, Arrays.asList(f.getId()), begin.toLocalDate().atStartOfDay(), finish.toLocalDate().atTime(LocalTime.MAX));
                if (CollectionUtils.isNotEmpty(records)) {
                    for (int i = 0; i < records.size(); i++) {
                        if ((records.get(0).getBegin().isAfter(finish.minusMinutes(1L)) &&
                                workTime.getFrom().isBefore(begin.plusMinutes(1L).toLocalTime())) ||
                                (records.get(records.size() - 1).getFinish().isBefore(begin.plusMinutes(1L)) &&
                                        (workTime.getTo().isBefore(finish.minusMinutes(1L).toLocalTime())))) {
                            times.put(begin, f.getId());
                        }
                        if (records.get(i).getFinish().minusMinutes(1L).isBefore(begin) &&
                                records.get(i + 1).getBegin().plusMinutes(1L).isAfter(finish)) {
                            times.put(records.get(i).getFinish(), f.getId());
                        }
                    }
                }
            });
        }
        if (!times.isEmpty()) {
            Map.Entry<LocalDateTime, UUID> entry = ((TreeMap<LocalDateTime, UUID>) times).firstEntry();
            result.setSpaceID(entry.getValue());
            result.setTime(entry.getKey());
        }
        result.setRecord(dto);
        return result;
    }


    private void checkRecord(CarWashRecordDto dto) {
        if (dto != null) {
            if (dto.getPackageId() == null && CollectionUtils.isEmpty(dto.getServices())) {
                throw new ClientException(SERVICE_NOT_CHOOSE);
            }
            CarWashDto carWash = getCarWashByRecord(dto);
            if (dto.getWorkingSpaceId() == null) {
                throw new ClientException(WORKING_SPACE_ID_EMPTY);
            }
            WorkingSpaceDto workingSpace = carWash.getSpaces().stream().filter(ws -> ws.getId().equals(dto.getWorkingSpaceId())).findFirst().orElse(null);
            if (workingSpace == null) {
                throw new ClientException(WORKING_SPACE_NOT_FOUND_IN_THIS_CARWASH);
            }
            checkWorkingSpaceByOpportunityRecordToTime(dto.getBegin(), dto.getFinish(), dto.getWorkingSpaceId(), carWash);
        }
    }

    private void checkWorkingSpaceByOpportunityRecordToTime(LocalDateTime begin, LocalDateTime finish, UUID workingSpaceId, CarWashDto carWash) {
        WorkTimeDto workTime = getWorkTimeByCarWash(begin, carWash);
        if (!(workTime.getFrom().equals(LocalTime.MIN) && workTime.getTo().equals(LocalTime.MAX))) {
            if (begin.toLocalTime().isBefore(workTime.getFrom()) || finish.toLocalTime().isAfter(workTime.getTo())) {
                throw new ClientException(CAR_WASH_NOT_WORK_THIS_TIME);
            }
        }
        List<CarWashRecordEntity> records =
                repository.findByStatusRecordAndWorkingSpaceIdInAndBeginBetweenOrderByBegin(
                        StatusRecord.CREATED, Arrays.asList(workingSpaceId), begin.toLocalDate().atStartOfDay(), begin.toLocalDate().atTime(LocalTime.MAX));
        if (CollectionUtils.isNotEmpty(records)) {
            boolean result = false;
            int i = 0;
            result = records.get(0).getBegin().isAfter(finish.minusMinutes(1L)) &&
                    workTime.getFrom().isBefore(begin.plusMinutes(1L).toLocalTime());
            result = records.get(records.size() - 1).getFinish().isBefore(begin.plusMinutes(1L)) &&
                    (workTime.getTo().isBefore(finish.minusMinutes(1L).toLocalTime())) && !result;
            while (!result) {
                result = records.get(i).getFinish().minusMinutes(1L).isBefore(begin) &&
                        records.get(i + 1).getBegin().plusMinutes(1L).isAfter(finish) && !result;
                i++;
            }
            if (!result) {
                throw new ClientException(NOT_ENOUGH_TIME_FOR_RECORD);
            }
        }
    }

    private WorkTimeDto getWorkTimeByCarWash(LocalDateTime start, CarWashDto carWash) {
        WorkTimeDto workTime = carWash.getWorkTimes().stream().filter(wt -> wt.getDayOfWeek().equals(start.getDayOfWeek())).findFirst().orElse(null);
        if (workTime == null || !workTime.getIsWork()) {
            throw new ClientException(CAR_WASH_NOT_WORK_THIS_DAY);
        }
        return workTime;
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

    private Long getDurationByRecord(CarWashRecordDto dto) {
        Long result = 0L;
        if (dto != null && CollectionUtils.isNotEmpty(dto.getServicesIds())) {
            List<ServicePriceDto> services = servicePriceService.getByIds(dto.getServicesIds());
            if (CollectionUtils.isNotEmpty(services)) {
                result += services.stream().mapToInt(ServicePriceDto::getDuration).sum();
            } else throw new ClientException(SERVICE_NOT_FOUND);
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

    private CarWashDto getCarWashByRecord(CarWashRecordDto dto) {
        if (dto.getCarWashId() == null) {
            throw new ClientException(CARWASH_ID_EMPTY);
        }
        CarWashDto carWash = carWashService.getById(dto.getCarWashId());
        if (carWash == null) {
            throw new ClientException(CARWASH_NOT_FOUND);
        }
        return carWash;
    }

}
