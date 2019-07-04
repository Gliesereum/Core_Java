package com.gliesereum.karma.service.record.impl;

import com.gliesereum.karma.aspect.annotation.RecordCreate;
import com.gliesereum.karma.aspect.annotation.RecordUpdate;
import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.karma.model.entity.record.BaseRecordPageEntity;
import com.gliesereum.karma.model.repository.jpa.record.BaseRecordRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.BusinessCategoryService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.business.WorkingSpaceService;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.karma.service.record.OrderService;
import com.gliesereum.karma.service.record.RecordServiceService;
import com.gliesereum.karma.service.service.PackageService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkTimeDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.WorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.*;
import com.gliesereum.share.common.model.dto.karma.record.*;
import com.gliesereum.share.common.model.dto.karma.service.LitePackageDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_AUTHENTICATION;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class BaseRecordServiceImpl extends DefaultServiceImpl<BaseRecordDto, BaseRecordEntity> implements BaseRecordService {

    private static final Class<BaseRecordDto> DTO_CLASS = BaseRecordDto.class;
    private static final Class<BaseRecordEntity> ENTITY_CLASS = BaseRecordEntity.class;

    private final BaseRecordRepository baseRecordRepository;

    @Autowired
    private CarService carService;

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private WorkingSpaceService workingSpaceService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    private RecordServiceService recordServiceService;

    @Autowired
    private BusinessCategoryService businessCategoryService;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private UserExchangeService exchangeService;

    @Autowired
    public BaseRecordServiceImpl(BaseRecordRepository baseRecordRepository, DefaultConverter defaultConverter) {
        super(baseRecordRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.baseRecordRepository = baseRecordRepository;
    }

    @Override
    @Transactional
    public List<BaseRecordDto> getByBusinessIdAndStatusRecord(UUID businessId, StatusRecord status, LocalDateTime from, LocalDateTime to) {
        List<BaseRecordEntity> entities = baseRecordRepository.findByBusinessIdAndStatusRecordAndBeginBetween(businessId, status, from, to);
        return setServicePrice(converter.convert(entities, dtoClass));
    }

    @Override
    @Transactional
    public List<BaseRecordDto> getByBusinessIdAndStatusRecordNotificationSend(UUID businessId, StatusRecord status, LocalDateTime from, LocalDateTime to, boolean notificationSend) {
        List<BaseRecordEntity> entities = baseRecordRepository.findByBusinessIdAndStatusRecordAndBeginBetweenAndNotificationSend(businessId, status, from, to, notificationSend);
        return converter.convert(entities, dtoClass);
    }

    @Override
    @Transactional
    public List<BaseRecordDto> getByTimeBetween(LocalDateTime from, Integer minutesFrom, Integer minutesTo, StatusRecord status, boolean notificationSend) {
        List<BaseRecordEntity> entities = baseRecordRepository.getByTimeBetween(from, minutesFrom, minutesTo, status, notificationSend);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public void setNotificationSend(UUID recordId) {
        Optional<BaseRecordEntity> byId = baseRecordRepository.findById(recordId);
        if (byId.isPresent()) {
            BaseRecordEntity entity = byId.get();
            entity.setNotificationSend(true);
            baseRecordRepository.saveAndFlush(entity);
        }
    }

    @Override
    public List<UUID> getCustomerIdsByBusinessIds(List<UUID> ids) {
        return baseRecordRepository.getCustomerIdsByBusinessIds(ids);
    }

    @Override
    public LiteRecordPageDto getLiteByParamsForBusiness(RecordsSearchDto search) {
        List<UUID> businessIds = null;
        LiteRecordPageDto result = new LiteRecordPageDto();
        if (CollectionUtils.isNotEmpty(search.getCorporationIds())) {
            businessIds = baseBusinessService.getIdsByCorporationIds(search.getCorporationIds());
        }
        if (CollectionUtils.isNotEmpty(businessIds) || CollectionUtils.isNotEmpty(search.getBusinessIds())) {
            search.getBusinessIds().addAll(businessIds);
            BaseRecordPageEntity entity = baseRecordRepository.getRecordsBySearchDto(search);
            result.setCount(entity.getCount());
            result.setRecords(convertToLiteRecordDto(entity.getRecords()));
        }
        return result;
    }

    @Override
    public Map<UUID, Set<RecordFreeTime>> getFreeTimes(UUID businessId, Long from, UUID packageId, List<UUID> serviceIds) {
        Map<UUID, Set<RecordFreeTime>> result = new HashMap<>();
        LocalDateTime begin = null;
        LocalDateTime finish = null;
        if (from != null) {
            begin = Instant.ofEpochMilli(from).atZone(ZoneId.of("UTC")).toLocalDateTime();
        } else {
            begin = LocalDateTime.now();
        }
        BaseBusinessDto business = baseBusinessService.getById(businessId);
        if (business == null) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        Long duration = getDurationByRecord(serviceIds, packageId);
        finish = begin.plusMinutes(duration);
        List<RecordFreeTime> freeTimes = getFreeTimesByBusinessAndCheckWorkingTime(begin, finish, business);
        if (CollectionUtils.isNotEmpty(freeTimes)) {
            LocalDateTime finalBegin = begin;
            freeTimes.forEach(f -> {
                if (f.getFinish().minusMinutes(duration).isAfter(finalBegin) && f.getMin() >= duration) {
                    putToMapIfKeyNotNull(result, f.getWorkingSpaceID(), f);
                }
            });
        }
        return result;
    }

    private void putToMapIfKeyNotNull(Map<UUID, Set<RecordFreeTime>> map, UUID key, RecordFreeTime time) {
        if (key != null) {
            Set<RecordFreeTime> value = map.getOrDefault(key, new TreeSet<>(Comparator.comparing(RecordFreeTime::getBegin)));
            value.add(time);
            map.put(key, value);
        }
    }

    @Override
    public List<LiteRecordDto> convertToLiteRecordDto(List<BaseRecordEntity> entities) {
        List<LiteRecordDto> result = converter.convert(entities, LiteRecordDto.class);
        return setServicePriceIds(result);
    }

    @Override
    public List<LiteRecordDto> getLiteRecordDtoByBusiness(UUID businessId, List<StatusRecord> statuses, Long from, Long to) {
        if (businessId == null) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        if (!baseBusinessService.currentUserHavePermissionToActionInBusinessLikeOwner(businessId)) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
        LocalDateTime fromDate;
        LocalDateTime toDate;
        if (CollectionUtils.isEmpty(statuses)) {
            statuses = Arrays.asList(StatusRecord.values());
        }
        if (from != null) {
            fromDate = Instant.ofEpochMilli(from).atZone(ZoneId.of("UTC")).toLocalDateTime();
        } else {
            fromDate = LocalDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay();
        }
        if (to != null) {
            toDate = Instant.ofEpochMilli(to).atZone(ZoneId.of("UTC")).toLocalDateTime();
        } else {
            toDate = fromDate.plusDays(1L);
        }
        if (fromDate.isAfter(toDate)) {
            throw new ClientException(TIME_IS_NOT_CORRECT);
        }
        List<BaseRecordEntity> entities = baseRecordRepository.findByBusinessIdAndStatusRecordInAndBeginBetween(businessId, statuses, fromDate, toDate);
        return converter.convert(entities, LiteRecordDto.class);
    }

    @Override
    public List<BaseRecordDto> getByParamsForClient(RecordsSearchDto search) {
        if (CollectionUtils.isEmpty(search.getTargetIds())) {
            throw new ClientException(TARGET_ID_IS_EMPTY);
        }
        if (businessCategoryService.checkAndGetType(search.getBusinessCategoryId()).equals(BusinessType.CAR)) {
            search.getTargetIds().forEach(carService::checkCarExistInCurrentUser);
        } else return Collections.emptyList(); //todo when add new service type need to add logic
        setSearch(search);
        List<BaseRecordEntity> entities = baseRecordRepository.findByStatusRecordInAndStatusProcessInAndTargetIdInAndBeginBetweenOrderByBeginDesc(
                search.getStatus(), search.getProcesses(), search.getTargetIds(), search.getFrom(), search.getTo());
        List<BaseRecordDto> result = converter.convert(entities, dtoClass);
        setFullModelRecord(result);
        return setServicePrice(result);
    }

    private void setClients(List<BaseRecordDto> result) {
        if (CollectionUtils.isNotEmpty(result)) {
            Set<UUID> clientIds = result.stream().map(BaseRecordDto::getClientId).collect(Collectors.toSet());
            Map<UUID, UserDto> clients = exchangeService.findUserMapByIds(clientIds);
            result.forEach(r -> {
                if (r.getClientId() != null) {
                    r.setClient(clients.get(r.getClientId()));
                }
            });
        }
    }

    @Override
    public List<BaseRecordDto> getByParamsForBusiness(RecordsSearchDto search) {
        List<BaseRecordDto> result = null;
        if (CollectionUtils.isEmpty(search.getBusinessIds())) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        search.getBusinessIds().forEach(f -> {
            if (!baseBusinessService.currentUserHavePermissionToActionInBusinessLikeOwner(f) &&
                    !baseBusinessService.currentUserHavePermissionToActionInBusinessLikeWorker(f)) {
                throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
            }
        });
        setSearch(search);
        List<BaseRecordEntity> entities = baseRecordRepository.findByStatusRecordInAndStatusProcessInAndBusinessIdInAndBeginBetweenOrderByBegin(
                search.getStatus(), search.getProcesses(), search.getBusinessIds(), search.getFrom(), search.getTo());
        result = converter.convert(entities, dtoClass);
        setClients(result);
        setFullModelRecord(result);
        setServicePrice(result);
        if (CollectionUtils.isNotEmpty(result)) {
            result = result.stream().sorted(Comparator.comparing(AbstractRecordDto::getBegin).reversed()).collect(Collectors.toList());
        }
        return result;
    }

    @Override
    public void setFullModelRecord(List<BaseRecordDto> list) {
        if (CollectionUtils.isNotEmpty(list)) {
            Map<UUID, PackageDto> packageMap = new HashMap<>();
            Map<UUID, BaseBusinessDto> businessMap = new HashMap<>();
            list.forEach(f -> {

                BaseBusinessDto business = businessMap.get(f.getBusinessId());
                if (business == null) {
                    business = baseBusinessService.getByIdIgnoreState(f.getBusinessId());
                    if (business != null) {
                        f.setBusiness(business);
                        businessMap.put(business.getId(), business);
                    }
                } else {
                    f.setBusiness(business);
                }

                PackageDto packageDto = packageMap.get(f.getPackageId());
                if (packageDto == null) {
                    packageDto = packageService.getByIdIgnoreState(f.getPackageId());
                    if (packageDto != null) {
                        f.setPackageDto(packageDto);
                        packageMap.put(packageDto.getId(), packageDto);
                    }
                } else {
                    f.setPackageDto(packageDto);
                }

            });
        }
    }

    @Override
    @Transactional
    @RecordUpdate
    public BaseRecordDto updateTimeRecord(UUID idRecord, Long beginTime) {

        LocalDateTime begin = LocalDateTime.ofInstant(Instant.ofEpochMilli(beginTime),
                TimeZone.getDefault().toZoneId());

        BaseRecordDto dto = getById(idRecord);
        checkPermissionToUpdate(dto);
        dto.setBegin(begin);
        LocalDateTime finish = begin.plusMinutes(getDurationByRecord(dto.getServicesIds(), dto.getPackageId()));
        dto.setFinish(finish);
        checkRecord(dto);
        BaseRecordDto result = super.update(dto);
        setServicePrice(Arrays.asList(result));
        return result;
    }

    @Override
    @Transactional
    @RecordUpdate
    public BaseRecordDto updateWorkingSpace(UUID idRecord, UUID workingSpaceId) {
        BaseRecordDto dto = getById(idRecord);
        checkPermissionToUpdate(dto);
        BaseBusinessDto business = getBusinessByRecord(dto);
        if (dto.getWorkingSpaceId() == null) {
            throw new ClientException(WORKING_SPACE_ID_EMPTY);
        }
        WorkingSpaceDto workingSpace = workingSpaceService.getById(workingSpaceId);
        if (workingSpace == null) {
            throw new ClientException(WORKING_SPACE_NOT_FOUND);
        }
        checkWorkingSpaceByOpportunityRecordToTime(dto.getBegin(), dto.getFinish(), workingSpaceId, business);
        dto.setWorkingSpaceId(workingSpaceId);
        BaseRecordDto result = super.update(dto);
        setServicePrice(Arrays.asList(result));
        return result;
    }

    @Override
    @Transactional
    @RecordUpdate
    public BaseRecordDto updateStatusProgress(UUID idRecord, StatusProcess status) {
        BaseRecordDto dto = getById(idRecord);
        checkPermissionToUpdate(dto);
        if (dto.getStatusRecord().equals(StatusRecord.CANCELED) ||
                dto.getStatusRecord().equals(StatusRecord.COMPLETED)) {
            throw new ClientException(CAN_NOT_CHANGE_STATUS_CANCELED_OR_COMPLETED_RECORD);
        }
        dto.setStatusProcess(status);
        if (status.equals(StatusProcess.COMPLETED)) {
            dto.setStatusRecord(StatusRecord.COMPLETED);
            WorkerDto worker = workerService.findByUserIdAndBusinessId(SecurityUtil.getUserId(), dto.getBusinessId());
            if (worker == null) {
                throw new ClientException(WORKER_NOT_FOUND);
            }
            dto.setWorkerId(worker.getId());
        }
        if (status.equals(StatusProcess.CANCELED)) {
            dto.setStatusRecord(StatusRecord.CANCELED);
        }
        BaseRecordDto result = super.update(dto);
        setServicePrice(Arrays.asList(result));
        return result;
    }

    @Override
    @Transactional
    @RecordUpdate
    public BaseRecordDto canceledRecord(UUID idRecord, String message) {
        BaseRecordDto dto = getById(idRecord);
        checkPermissionToUpdate(dto);
        dto.setStatusRecord(StatusRecord.CANCELED);
        dto.setStatusProcess(StatusProcess.CANCELED);
        dto.setCanceledDescription(message);
        BaseRecordDto result = super.update(dto);
        setServicePrice(Arrays.asList(result));
        return result;
    }

    @Override
    @Transactional
    @RecordCreate
    public BaseRecordDto create(BaseRecordDto dto) {
        BaseRecordDto result = null;
        SecurityUtil.checkUserByBanStatus();
        if (dto != null) {
            setType(dto);
            if (businessCategoryService.checkAndGetType(dto.getBusinessCategoryId()).equals(BusinessType.CAR)) {
                if (dto.getTargetId() == null) {
                    throw new ClientException(TARGET_ID_IS_EMPTY);
                }
                carService.checkCarExistInCurrentUser(dto.getTargetId());
            }
            UserDto user = SecurityUtil.getUser().getUser();
            dto.setClientId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setPhone(user.getPhone());
            result = createRecord(dto);
        }
        return result;
    }

    @Override
    @Transactional
    public BaseRecordDto createFromBusiness(BaseRecordDto dto) {
        SecurityUtil.checkUserByBanStatus();
        if (dto.getBusinessId() != null &&
                !baseBusinessService.currentUserHavePermissionToActionInBusinessLikeWorker(dto.getBusinessId())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_RECORD);
        }
        if (dto.getClientId() != null) {
            List<PublicUserDto> users = exchangeService.findPublicUserByIds(Arrays.asList(dto.getClientId()));
            if (CollectionUtils.isNotEmpty(users)) {
                PublicUserDto user = users.get(0);
                dto.setLastName(user.getLastName());
                dto.setFirstName(user.getFirstName());
                dto.setPhone(user.getPhone());
                dto.setClientId(user.getId());
            }
        }
        return createRecord(dto);
    }

    @Override
    @Transactional
    public BaseRecordDto getFullModelByIdWithPermission(UUID id) {
        BaseRecordDto result = getFullModelById(id);
        checkPermissionToUpdate(result);
        return result;
    }


    @Override
    @Transactional
    public BaseRecordDto getFullModelById(UUID id) {
        BaseRecordDto result = null;
        BaseRecordDto byId = getById(id);
        if (byId != null) {
            List<BaseRecordDto> list = Arrays.asList(byId);
            setFullModelRecord(list);
            result = list.get(0);
        }
        setServicePrice(Arrays.asList(result));
        return result;
    }

    @Override
    public BaseRecordDto updateStatusPay(UUID idRecord, StatusPay status) {
        BaseRecordDto dto = getById(idRecord);
        checkPermissionToUpdate(dto);
        dto.setStatusPay(status);
        BaseRecordDto result = super.update(dto);
        setServicePrice(Arrays.asList(result));
        return result;
    }


    private BaseRecordDto createRecord(BaseRecordDto dto) {
        BaseRecordDto result = null;
        BaseBusinessDto business = getBusinessByRecord(dto);
        checkBeginTimeForRecord(dto.getBegin(), business.getTimeZone());
        dto.setFinish(dto.getBegin().plusMinutes(getDurationByRecord(dto.getServicesIds(), dto.getPackageId())));
        dto.setPrice(getPriceByRecord(dto));
        dto.setStatusRecord(StatusRecord.CREATED);
        dto.setStatusProcess(StatusProcess.WAITING);
        dto.setStatusPay(StatusPay.NOT_PAID);
        if (dto.getPayType() == null) {
            dto.setPayType(PayType.CASH);
        }
        //dto.setClientId(SecurityUtil.getUserId());
        dto.setBusinessCategoryId(business.getBusinessCategoryId());
        checkRecord(dto);
        dto.setId(null);
        setRecordNumber(dto);
        BaseRecordEntity entity = converter.convert(dto, entityClass);
        entity = repository.saveAndFlush(entity);
        if (entity != null) {
            for (UUID servicesId : dto.getServicesIds()) {
                recordServiceService.create(new RecordServiceDto(entity.getId(), servicesId));
            }
            baseRecordRepository.refresh(entity);
            result = converter.convert(entity, dtoClass);
            result.setServicesIds(dto.getServicesIds());
            setFullModelRecord(Arrays.asList(result));
            if (CollectionUtils.isNotEmpty(dto.getServicesIds())) {
                result.setServices(servicePriceService.getByIds(dto.getServicesIds()));
            }
            createOrders(result);
        }
        return result;
    }

    @Override
    public BaseRecordDto superCreateRecord(BaseRecordDto dto) { //todo for create records
        BaseRecordDto result = null;
        BaseRecordEntity entity = converter.convert(dto, entityClass);
        entity = repository.saveAndFlush(entity);
        if (entity != null) {
            for (UUID servicesId : dto.getServicesIds()) {
                recordServiceService.create(new RecordServiceDto(entity.getId(), servicesId));
            }
            baseRecordRepository.refresh(entity);
            result = converter.convert(entity, dtoClass);
            result.setServicesIds(dto.getServicesIds());
        }
        return result;
    }

    @Override
    public BaseRecordDto getFreeTimeForRecord(BaseRecordDto dto) {
        if (dto != null) {
            BaseBusinessDto business = getBusinessByRecord(dto);
            checkBeginTimeForRecord(dto.getBegin(), business.getTimeZone());
            Long duration = getDurationByRecord(dto.getServicesIds(), dto.getPackageId());
            dto.setFinish(dto.getBegin().plusMinutes(duration));
            List<RecordFreeTime> freeTimes = getFreeTimesByBusinessAndCheckWorkingTime(dto.getBegin(), dto.getFinish(), business);
            if (CollectionUtils.isNotEmpty(freeTimes)) {
                RecordFreeTime freeTime = getNearest(freeTimes, dto.getBegin(), duration);
                if (freeTime != null) {
                    dto.setWorkingSpaceId(freeTime.getWorkingSpaceID());
                    if (freeTime.getBegin().isAfter(dto.getBegin())) {
                        dto.setBegin(freeTime.getBegin());
                        dto.setFinish(dto.getBegin().plusMinutes(duration));
                    }
                    dto.setPrice(getPriceByRecord(dto));
                } else {
                    throw new ClientException(NOT_ENOUGH_TIME_FOR_RECORD);
                }
            } else {
                dto.setBegin(null);
                throw new ClientException(NOT_ENOUGH_TIME_FOR_RECORD);
            }
        }
        return dto;
    }

    private RecordFreeTime getNearest(List<RecordFreeTime> times, LocalDateTime begin, Long duration) {
        if (CollectionUtils.isNotEmpty(times)) {
            return times.stream()
                    .filter(f -> f.getMin() >= duration && f.getFinish().minusMinutes(duration).isAfter(begin))
                    .sorted(Comparator.comparing(RecordFreeTime::getBegin)).findFirst().orElse(null);
        }
        return null;
    }

    @Override
    public List<BaseRecordDto> getAllByUser() {
        SecurityUtil.checkUserByBanStatus();
        List<BaseRecordEntity> entities = baseRecordRepository.findAllByClientId(SecurityUtil.getUserId());
        List<BaseRecordDto> result = converter.convert(entities, dtoClass);
        setFullModelRecord(result);
        setServicePrice(result);
        if (CollectionUtils.isNotEmpty(result)) {
            result = result.stream().sorted(Comparator.comparing(AbstractRecordDto::getBegin).reversed()).collect(Collectors.toList());
        }
        return result;
    }

    private void checkRecord(BaseRecordDto dto) {
        if (dto != null) {
            checkServiceChoose(dto);
            BaseBusinessDto business = getBusinessByRecord(dto);
            if (dto.getWorkingSpaceId() == null) {
                throw new ClientException(WORKING_SPACE_ID_EMPTY);
            }
            WorkingSpaceDto workingSpace = business.getSpaces().stream().filter(ws -> ws.getId().equals(dto.getWorkingSpaceId())).findFirst().orElse(null);
            if (workingSpace == null) {
                throw new ClientException(WORKING_SPACE_NOT_FOUND_IN_THIS_BUSINESS);
            }
            checkWorkingSpaceByOpportunityRecordToTime(dto.getBegin(), dto.getFinish(), dto.getWorkingSpaceId(), business);
        }
    }

    private void checkWorkingSpaceByOpportunityRecordToTime(LocalDateTime begin, LocalDateTime finish, UUID workingSpaceId, BaseBusinessDto business) {
        List<RecordFreeTime> freeTimes = getFreeTimesByBusinessAndCheckWorkingTime(begin, finish, business);
        if (CollectionUtils.isNotEmpty(freeTimes)) {
            RecordFreeTime freeTime = freeTimes.stream()
                    .filter(f -> f.getWorkingSpaceID().equals(workingSpaceId) &&
                            begin.plusMinutes(1L).isAfter(f.getBegin()) &&
                            finish.minusMinutes(1L).isBefore(f.getFinish()))
                    .findFirst().orElse(null);
            if (freeTime == null) {
                throw new ClientException(NOT_ENOUGH_TIME_FOR_RECORD);
            }
        } else throw new ClientException(NOT_ENOUGH_TIME_FOR_RECORD);
    }

    private WorkTimeDto getWorkTimeByBusiness(LocalDateTime begin, BaseBusinessDto business) {
        WorkTimeDto workTime = business.getWorkTimes().stream().filter(wt -> wt.getDayOfWeek().equals(begin.getDayOfWeek())).findFirst().orElse(null);
        if (workTime == null || !workTime.getIsWork()) {
            throw new ClientException(BUSINESS_NOT_WORK_THIS_DAY);
        }
        return workTime;
    }

    private Integer getPriceByRecord(BaseRecordDto dto) {
        int result = 0;
        checkServiceChoose(dto);
        if (dto != null && dto.getPackageId() != null) {
            PackageDto packageDto = packageService.getById(dto.getPackageId());
            if (packageDto != null) {
                if (CollectionUtils.isEmpty(packageDto.getServices())) {
                    throw new ClientException(PACKAGE_NOT_HAVE_SERVICE);
                }
                int sumByPackage = packageDto.getServices().stream().mapToInt(ServicePriceDto::getPrice).sum();
                if (packageDto.getDiscount() > 0) {
                    result += (int) (sumByPackage - ((sumByPackage / 100.0f) * packageDto.getDiscount()));
                } else {
                    result += sumByPackage;
                }
            } else throw new ClientException(PACKAGE_NOT_FOUND);

        }
        if (dto != null && CollectionUtils.isNotEmpty(dto.getServicesIds())) {
            List<ServicePriceDto> services = servicePriceService.getByIds(dto.getServicesIds());
            if (CollectionUtils.isEmpty(services)) {
                throw new ClientException(SERVICE_NOT_FOUND);
            }
            result += services.stream().mapToInt(ServicePriceDto::getPrice).sum();
        }
        return result;
    }

    private Long getDurationByRecord(List<UUID> servicesIds, UUID packageId) {
        Long result = 0L;
        if (packageId == null && CollectionUtils.isEmpty(servicesIds)) {
            throw new ClientException(SERVICE_NOT_CHOOSE);
        }
        if (CollectionUtils.isNotEmpty(servicesIds)) {
            List<ServicePriceDto> services = servicePriceService.getByIds(servicesIds);
            if (CollectionUtils.isNotEmpty(services)) {
                result += services.stream().mapToInt(ServicePriceDto::getDuration).sum();
            } else throw new ClientException(SERVICE_NOT_FOUND);
        }
        if (packageId != null) {
            LitePackageDto packageDto = packageService.getLiteById(packageId);
            if (packageDto != null) {
                result += packageDto.getDuration();
            } else throw new ClientException(PACKAGE_NOT_FOUND);
        }
        return result;
    }

    private void checkServiceChoose(BaseRecordDto dto) {
        if (dto != null && dto.getPackageId() == null && CollectionUtils.isEmpty(dto.getServicesIds())) {
            throw new ClientException(SERVICE_NOT_CHOOSE);
        }
    }

    private void checkPermissionToUpdate(BaseRecordDto dto) {
        if (SecurityUtil.isAnonymous()) throw new ClientException(USER_NOT_AUTHENTICATION);
        if (dto == null) throw new ClientException(RECORD_NOT_FOUND);
        boolean ownerPermission = baseBusinessService.currentUserHavePermissionToActionInBusinessLikeOwner(dto.getBusinessId());
        boolean workerPermission = baseBusinessService.currentUserHavePermissionToActionInBusinessLikeWorker(dto.getBusinessId());
        boolean userPermission = false;
        if (dto.getClientId().equals(SecurityUtil.getUserId())) {
            userPermission = true;
        }
        if (!BooleanUtils.or(new Boolean[]{ownerPermission, workerPermission, userPermission})) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_RECORD);
        }
    }

    private void createOrders(BaseRecordDto dto) {
        if (dto != null && CollectionUtils.isNotEmpty(dto.getServicesIds())) {
            List<ServicePriceDto> services = servicePriceService.getByIds(dto.getServicesIds());
            services.forEach(f -> orderService.create(new OrderDto(f.getServiceId(), dto.getId(), f.getPrice(), false)));
        }
        if (dto != null && dto.getPackageId() != null) {
            PackageDto packageDto = packageService.getById(dto.getPackageId());
            if (packageDto != null) {
                if (CollectionUtils.isEmpty(packageDto.getServices())) {
                    throw new ClientException(PACKAGE_NOT_HAVE_SERVICE);
                }
                packageDto.getServices().forEach(f -> {
                    int price = f.getPrice() - ((f.getPrice() / 100) * packageDto.getDiscount());
                    orderService.create(new OrderDto(f.getServiceId(), dto.getId(), price, true));
                });
            } else throw new ClientException(PACKAGE_NOT_FOUND);
        }
    }

    private BaseBusinessDto getBusinessByRecord(BaseRecordDto dto) {
        if (dto.getBusinessId() == null) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        BaseBusinessDto business = baseBusinessService.getById(dto.getBusinessId());
        if (business == null) {
            throw new ClientException(BUSINESS_NOT_FOUND);
        }
        return business;
    }

    private void checkBeginTimeForRecord(LocalDateTime time, Integer timeZoneOffset) {
        if (time == null) {
            throw new ClientException(TIME_BEGIN_EMPTY);
        }
        LocalDateTime currentTime = LocalDateTime.now(ZoneOffset.UTC).plusMinutes(timeZoneOffset);
        if (time.plusMinutes(3L).isBefore(currentTime)) {
            throw new ClientException(TIME_BEGIN_PAST);
        }
    }

    private void checkTimeWorking(WorkTimeDto workTime, LocalDateTime begin, LocalDateTime finish) {
        if (!(workTime.getFrom().equals(LocalTime.MIN) && workTime.getTo().equals(LocalTime.MAX))) {
            if (begin.toLocalTime().minusMinutes(1L).isBefore(workTime.getFrom()) || finish.toLocalTime().plusMinutes(1L).isAfter(workTime.getTo())) {
                throw new ClientException(BUSINESS_NOT_WORK_THIS_TIME);
            }
        }
    }

    private void setType(BaseRecordDto dto) {
        BaseBusinessDto business = getBusinessByRecord(dto);
        dto.setBusinessCategoryId(business.getBusinessCategoryId());
    }

    private List<RecordFreeTime> getFreeTimesByBusinessAndCheckWorkingTime(LocalDateTime begin, LocalDateTime finish, BaseBusinessDto business) {
        WorkTimeDto workTime = getWorkTimeByBusiness(begin, business);
        checkTimeWorking(workTime, begin, finish);
        return getFreeTimesByBusiness(business, begin.toLocalDate().atTime(workTime.getFrom()), begin.toLocalDate().atTime(workTime.getTo()));
    }

    private List<RecordFreeTime> getFreeTimesByBusiness(BaseBusinessDto business, LocalDateTime startTimeWork, LocalDateTime endTimeWork) {
        List<RecordFreeTime> result = new ArrayList();
        if (business != null && CollectionUtils.isNotEmpty(business.getSpaces())) {
            List<BaseRecordEntity> records = baseRecordRepository.findByBusinessIdAndStatusRecordAndBeginBetween(business.getId(), StatusRecord.CREATED, startTimeWork, endTimeWork);
            business.getSpaces().forEach(b -> {
                UUID currentId = b.getId();
                if (CollectionUtils.isNotEmpty(records)) {
                    List<BaseRecordEntity> recordsBySpace = records.stream().filter(f -> f.getWorkingSpaceId().equals(currentId)).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(recordsBySpace)) {
                        for (int i = 0; i < recordsBySpace.size(); i++) {
                            LocalDateTime begin = null;
                            LocalDateTime end = null;
                            if (i == 0) {
                                begin = startTimeWork;
                                end = recordsBySpace.get(i).getBegin();
                            }
                            if (recordsBySpace.size() > 1 && i != 0 && i != recordsBySpace.size() - 1) {
                                begin = recordsBySpace.get(i - 1).getFinish();
                                end = recordsBySpace.get(i).getBegin();
                            }
                            if (i == recordsBySpace.size() - 1 || recordsBySpace.size() == 1) {
                                begin = recordsBySpace.get(i).getFinish();
                                end = endTimeWork;
                            }
                            if (!begin.equals(end) && begin.isBefore(end)) {
                                result.add(new RecordFreeTime(currentId, begin, end));
                            }
                        }
                    } else {
                        result.add(new RecordFreeTime(currentId, startTimeWork, endTimeWork));
                    }
                } else {
                    result.add(new RecordFreeTime(currentId, startTimeWork, endTimeWork));
                }
            });
        }
        return result;
    }

    private List<BaseRecordDto> setServicePrice(List<BaseRecordDto> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            List<UUID> recordIds = records.stream()
                    .filter(i -> i.getId() != null)
                    .map(BaseRecordDto::getId)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(recordIds)) {
                Map<UUID, List<ServicePriceDto>> recordServices = recordServiceService.getServicePriceMap(recordIds);
                if (MapUtils.isNotEmpty(recordServices)) {
                    records.forEach(record -> {
                        if (record.getServices() == null) {
                            record.setServices(new ArrayList<>());
                        }
                        record.setServices(recordServices.get(record.getId()));

                    });
                }
            }
        }
        return records;
    }

    private List<LiteRecordDto> setServicePriceIds(List<LiteRecordDto> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            Map<UUID, List<UUID>> mapIds = recordServiceService.getServicePriceIds(records.stream().map(LiteRecordDto::getId).collect(Collectors.toList()));
            records.forEach(r -> r.setServicesIds(mapIds.get(r.getId())));
        }
        return records;
    }

    private void setRecordNumber(BaseRecordDto dto) {
        if (dto != null) {
            LocalDateTime startOfDay = LocalDateTime.of(dto.getBegin().toLocalDate(), LocalTime.MIDNIGHT);
            LocalDateTime endOfDay = LocalDateTime.of(dto.getBegin().toLocalDate(), LocalTime.MAX);
            long recordToDay = baseRecordRepository.countByBusinessIdAndBeginBetween(dto.getBusinessId(), startOfDay, endOfDay);
            dto.setRecordNumber((int) recordToDay + 1);
        }
    }

    private void setSearch(RecordsSearchDto search) {
        if (search == null) {
            search = new RecordsSearchDto();
        }
        if (CollectionUtils.isEmpty(search.getStatus())) {
            search.setStatus(Arrays.asList(StatusRecord.values()));
        }
        if (CollectionUtils.isEmpty(search.getProcesses())) {
            search.setProcesses(Arrays.asList(StatusProcess.values()));
        }
        if (search.getFrom() == null) {
            search.setFrom(LocalDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay());
        }
        if (search.getTo() == null) {
            search.setTo(search.getFrom().plusYears(1L));
        }
    }
}
