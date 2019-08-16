package com.gliesereum.karma.service.record.impl;

import com.gliesereum.karma.aspect.annotation.RecordCreate;
import com.gliesereum.karma.aspect.annotation.RecordUpdate;
import com.gliesereum.karma.facade.business.BusinessPermissionFacade;
import com.gliesereum.karma.facade.client.ClientFacade;
import com.gliesereum.karma.model.common.BusinessPermission;
import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.karma.model.repository.jpa.record.BaseRecordRepository;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.BusinessCategoryService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.es.ClientEsService;
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
import com.gliesereum.share.common.model.dto.karma.client.ClientDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.*;
import com.gliesereum.share.common.model.dto.karma.record.*;
import com.gliesereum.share.common.model.dto.karma.record.search.BusinessRecordSearchDto;
import com.gliesereum.share.common.model.dto.karma.record.search.BusinessRecordSearchPageableDto;
import com.gliesereum.share.common.model.dto.karma.record.search.ClientRecordSearchDto;
import com.gliesereum.share.common.model.dto.karma.service.LitePackageDto;
import com.gliesereum.share.common.model.dto.karma.service.PackageDto;
import com.gliesereum.share.common.model.dto.karma.service.ServicePriceDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private ClientEsService clientEsService;

    @Autowired
    private ClientFacade clientFacade;

    @Autowired
    private BusinessPermissionFacade businessPermissionFacade;

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
    public Map<UUID, Set<RecordFreeTime>> getFreeTimes(UUID businessId, UUID workerId, Long from, UUID packageId, List<UUID> serviceIds) {
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
        Long duration = getDurationByRecord(serviceIds, packageId, businessId);
        finish = begin.plusMinutes(duration);
        List<RecordFreeTime> freeTimes = getFreeTimesByBusinessAndCheckWorkingTime(begin, finish, business, workerId);
        if (CollectionUtils.isNotEmpty(freeTimes)) {
            LocalDateTime finalBegin = begin;
            freeTimes.forEach(f -> {
                if (f.getFinish().minusMinutes(duration).isAfter(finalBegin) && f.getMin() >= duration) {
                    putToMapIfKeyNotNull(result, f.getWorkerID(), f);
                }
            });
        }
        return result;
    }

    @Override
    public Page<BaseRecordDto> getByClientForCorporation(List<UUID> corporationIds, UUID clientId, Integer page, Integer size) {
        corporationIds.forEach(f -> businessPermissionFacade.checkPermissionByCorporation(f, BusinessPermission.VIEW_BUSINESS_INFO));
        Page<BaseRecordDto> result = null;
        List<UUID> businessIds = baseBusinessService.getIdsByCorporationIds(corporationIds);
        if (CollectionUtils.isNotEmpty(businessIds)) {
            if (page == null) page = 0;
            if (size == null) size = 50;
            Page<BaseRecordEntity> entities = baseRecordRepository.findAllByBusinessIdInAndClientIdOrderByBeginDesc(businessIds, clientId, PageRequest.of(page, size));
            if (entities != null && CollectionUtils.isNotEmpty(entities.getContent())) {
                result = converter.convert(entities, dtoClass);
                setServicePrice(result.getContent());
                setFullModelRecord(result.getContent());
            }
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
    public List<BaseRecordDto> convertToLiteRecordDto(List<BaseRecordEntity> entities) {
        List<BaseRecordDto> result = converter.convert(entities, BaseRecordDto.class);
        return setServicePriceIds(result);
    }

    @Override
    public List<BaseRecordDto> getByParamsForClient(ClientRecordSearchDto search) {
        SecurityUtil.checkUserByBanStatus();
        if (CollectionUtils.isEmpty(search.getTargetIds())) {
            throw new ClientException(TARGET_ID_IS_EMPTY);
        }
        BusinessType businessType = businessCategoryService.checkAndGetType(search.getBusinessCategoryId());
        switch (businessType) {
            case CAR: {
                search.getTargetIds().forEach(carService::checkCarExistInCurrentUser);
                break;
            }
            case HUMAN: {
                search.setTargetIds(Arrays.asList(SecurityUtil.getUserId()));
                break;
            }
        }
        setSearch(search);
        List<BaseRecordEntity> entities = baseRecordRepository.findByStatusRecordInAndStatusProcessInAndTargetIdInAndBeginBetweenOrderByBeginDesc(
                search.getStatus(), search.getProcesses(), search.getTargetIds(), search.getFrom(), search.getTo());
        List<BaseRecordDto> result = converter.convert(entities, dtoClass);
        setFullModelRecord(result);
        return setServicePrice(result);
    }

    private void setClients(List<BaseRecordDto> result) {
        if (CollectionUtils.isNotEmpty(result)) {
            Set<UUID> clientIds = result.stream().filter(i -> i.getClientId() != null).map(BaseRecordDto::getClientId).collect(Collectors.toSet());
            Set<UUID> businessIds = result.stream().map(BaseRecordDto::getBusinessId).collect(Collectors.toSet());
            Map<UUID, ClientDto> clientMap = clientFacade.getClientMapByIds(clientIds, businessIds);
            result.forEach(r -> {
                if (r.getClientId() != null) {
                    r.setClient(clientMap.get(r.getClientId()));
                }
            });
        }
    }

    @Override
    public Page<BaseRecordDto> getByParamsForBusiness(BusinessRecordSearchPageableDto search) {
        Page<BaseRecordDto> result = null;
        processSearchForBusinessModel(search);
        Pageable pageable = getPageable(search);
        Page<BaseRecordEntity> entities = baseRecordRepository.getRecordsBySearchDto(search, pageable);
        result = converter.convert(entities, dtoClass);
        if ((result != null) && (CollectionUtils.isNotEmpty(result.getContent()))) {
            setClients(result.getContent());
            setFullModelRecord(result.getContent());
            setServicePrice(result.getContent());
        }
        return result;
    }

    @Override
    public RecordPaymentInfoDto getPaymentInfoForBusiness(BusinessRecordSearchDto search) {
        processSearchForBusinessModel(search);
        return baseRecordRepository.getPaymentInfoBySearch(search);
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
    public BaseRecordDto updateRecordTime(UUID idRecord, Long beginTime) {

        LocalDateTime begin = LocalDateTime.ofInstant(Instant.ofEpochMilli(beginTime),
                TimeZone.getDefault().toZoneId());

        BaseRecordDto dto = getById(idRecord);
        checkPermissionToUpdate(dto);
        dto.setBegin(begin);
        setServicePriceIds(Arrays.asList(dto));
        LocalDateTime finish = begin.plusMinutes(getDurationByRecord(dto.getServicesIds(), dto.getPackageId(), dto.getBusinessId()));
        dto.setFinish(finish);
        checkRecord(dto);
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
            BusinessType businessType = businessCategoryService.checkAndGetType(dto.getBusinessCategoryId());
            switch (businessType) {
                case CAR: {
                    if (dto.getTargetId() == null) {
                        throw new ClientException(TARGET_ID_IS_EMPTY);
                    }
                    carService.checkCarExistInCurrentUser(dto.getTargetId());
                    break;
                }
                case HUMAN: {
                    dto.setTargetId(SecurityUtil.getUserId());
                    break;
                }
            }
            UserDto user = SecurityUtil.getUser().getUser();
            dto.setClientId(user.getId());
            result = createRecord(dto);
            clientEsService.addNewClient(user, dto.getBusinessId());
        }
        return result;
    }

    @Override
    @Transactional
    @RecordCreate
    public BaseRecordDto createForBusiness(BaseRecordDto dto) {
        businessPermissionFacade.checkPermissionByBusiness(dto.getBusinessId(), BusinessPermission.WORK_WITH_RECORD);
        BaseRecordDto record = createRecord(dto);
        setClients(Arrays.asList(record));
        if (dto.getClientId() != null) {
            List<PublicUserDto> users = exchangeService.findPublicUserByIds(Arrays.asList(dto.getClientId()));
            if (CollectionUtils.isNotEmpty(users)) {
                PublicUserDto user = users.get(0);
                dto.setClientId(user.getId());
                clientEsService.addNewClient(user, dto.getBusinessId());
            }
        }
        return record;
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
            setClients(list);
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
        dto.setFinish(dto.getBegin().plusMinutes(getDurationByRecord(dto.getServicesIds(), dto.getPackageId(), dto.getBusinessId())));
        dto.setPrice(getPriceByRecord(dto));
        dto.setStatusRecord(StatusRecord.CREATED);
        dto.setStatusProcess(StatusProcess.WAITING);
        dto.setStatusPay(StatusPay.NOT_PAID);
        if (dto.getPayType() == null) {
            dto.setPayType(PayType.CASH);
        }
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
            if (dto.getBegin() != null) {
                int mod = 10 - (dto.getBegin().getMinute() % 10);
                if (mod < 5) {
                    mod = mod + 5;
                }
                dto.setBegin(dto.getBegin().plusMinutes(mod).withSecond(0));
            }
            checkBeginTimeForRecord(dto.getBegin(), business.getTimeZone());
            Long duration = getDurationByRecord(dto.getServicesIds(), dto.getPackageId(), dto.getBusinessId());
            dto.setFinish(dto.getBegin().plusMinutes(duration));
            List<RecordFreeTime> freeTimes = getFreeTimesByBusinessAndCheckWorkingTime(dto.getBegin(), dto.getFinish(), business, dto.getWorkerId());
            if (CollectionUtils.isNotEmpty(freeTimes)) {
                RecordFreeTime freeTime = getNearest(freeTimes, dto.getBegin(), duration);
                if (freeTime != null) {
                    dto.setWorkingSpaceId(freeTime.getWorkingSpaceID());
                    dto.setWorkerId(freeTime.getWorkerID());
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
    public Page<BaseRecordDto> getAllByUser(Integer page, Integer size) {
        SecurityUtil.checkUserByBanStatus();
        if (page == null) page = 0;
        if (size == null) size = 20;
        Page<BaseRecordDto> result = null;
        Page<BaseRecordEntity> entities = baseRecordRepository.findAllByClientId(SecurityUtil.getUserId(), PageRequest.of(page, size, Sort.by("begin").descending()));
        if (entities != null && CollectionUtils.isNotEmpty(entities.getContent())) {
            result = converter.convert(entities, dtoClass);
            setServicePrice(result.getContent());
            setFullModelRecord(result.getContent());
        }
        return result;
    }

    private void processSearchForBusinessModel(BusinessRecordSearchDto search) {
        if (CollectionUtils.isEmpty(search.getBusinessIds()) && (search.getCorporationId() == null)) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        UUID workingSpaceId = null;
        if (search.getCorporationId() != null) {
            businessPermissionFacade.checkPermissionByCorporation(search.getCorporationId(), BusinessPermission.VIEW_BUSINESS_INFO);
            List<UUID> businessIds = baseBusinessService.getIdsByCorporationIds(Arrays.asList(search.getCorporationId()));
            search.setBusinessIds(businessIds);
        } else if (search.getBusinessIds().size() == 1) {
            businessPermissionFacade.checkPermissionByBusiness(search.getBusinessIds().get(0), BusinessPermission.VIEW_BUSINESS_INFO);
            workingSpaceId = getWorkingSpaceIfWorkerOrCheckPermissionToViewAll(search.getBusinessIds().get(0));
        } else {
            businessPermissionFacade.checkPermissionByBusiness(search.getBusinessIds(), BusinessPermission.VIEW_BUSINESS_INFO);
        }
        if (workingSpaceId != null) {
            search.setWorkingSpaceIds(Arrays.asList(workingSpaceId));
        }
    }

    private void checkRecord(BaseRecordDto dto) {
        if (dto != null) {
            checkServiceChoose(dto);
            BaseBusinessDto business = getBusinessByRecord(dto);
            if (dto.getWorkingSpaceId() == null) {
                throw new ClientException(WORKING_SPACE_ID_EMPTY);
            }
            if (dto.getWorkerId() == null) {
                throw new ClientException(WORKER_ID_EMPTY);
            }
            WorkingSpaceDto workingSpace = business.getSpaces().stream().filter(ws -> ws.getId().equals(dto.getWorkingSpaceId())).findFirst().orElse(null);
            if (workingSpace == null) {
                throw new ClientException(WORKING_SPACE_NOT_FOUND_IN_THIS_BUSINESS);
            }
            checkWorkingSpaceByOpportunityRecordToTime(dto.getBegin(), dto.getFinish(), dto.getWorkerId(), business);
        }
    }

    private void checkWorkingSpaceByOpportunityRecordToTime(LocalDateTime begin, LocalDateTime finish, UUID workerId, BaseBusinessDto business) {
        List<RecordFreeTime> freeTimes = getFreeTimesByBusinessAndCheckWorkingTime(begin, finish, business, workerId);
        if (CollectionUtils.isNotEmpty(freeTimes)) {
            RecordFreeTime freeTime = freeTimes.stream()
                    .filter(f -> begin.plusMinutes(1L).isAfter(f.getBegin()) &&
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

    private Long getDurationByRecord(List<UUID> servicesIds, UUID packageId, UUID businessId) {
        Long result = 0L;
        if (packageId == null && CollectionUtils.isEmpty(servicesIds)) {
            throw new ClientException(SERVICE_NOT_CHOOSE);
        }
        if (CollectionUtils.isNotEmpty(servicesIds)) {
            List<ServicePriceDto> services = servicePriceService.getByIds(servicesIds);
            if (CollectionUtils.isNotEmpty(services)) {
                if (services.stream().anyMatch(s -> !s.getBusinessId().equals(businessId))) {
                    throw new ClientException(SERVICE_PRICE_NOT_FOUND_IN_BUSINESS);
                }
                result += services.stream().mapToInt(ServicePriceDto::getDuration).sum();
            } else throw new ClientException(SERVICE_NOT_FOUND);
        }
        if (packageId != null) {
            LitePackageDto packageDto = packageService.getLiteById(packageId);
            if (packageDto != null) {
                if (!packageDto.getBusinessId().equals(businessId)) {
                    throw new ClientException(PACKAGE_NOT_FOUND_IN_BUSINESS);
                }
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
        boolean workerPermission = businessPermissionFacade.isHavePermissionByBusiness(dto.getBusinessId(), BusinessPermission.WORK_WITH_RECORD);
        boolean userPermission = false;
        if ((dto.getClientId() != null) && dto.getClientId().equals(SecurityUtil.getUserId())) {
            userPermission = true;
        }
        if (!BooleanUtils.or(new Boolean[]{workerPermission, userPermission})) {
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

    private void checkBusinessTimeWorking(WorkTimeDto workTime, LocalDateTime begin, LocalDateTime finish) {
        if (!(workTime.getFrom().equals(LocalTime.MIN) && workTime.getTo().equals(LocalTime.MAX))) {
            if (workTime.getTo().isBefore(begin.toLocalTime()) || workTime.getFrom().isAfter(begin.toLocalTime())) {
                throw new ClientException(BUSINESS_NOT_WORK_THIS_TIME);
            }
            if (workTime.getTo().isBefore(finish.toLocalTime())) {
                throw new ClientException(NOT_ENOUGH_TIME_FOR_RECORD);
            }
        }
    }

    private void checkWorkerTimeWorking(WorkTimeDto workTime, LocalDateTime begin) {
        if (workTime == null || (workTime.getTo().isBefore(begin.toLocalTime()) || workTime.getFrom().isAfter(begin.toLocalTime()))) {
            throw new ClientException(WORKER_NOT_WORK_THIS_TIME);
        }
    }

    private void setType(BaseRecordDto dto) {
        BaseBusinessDto business = getBusinessByRecord(dto);
        dto.setBusinessCategoryId(business.getBusinessCategoryId());
    }

    private List<RecordFreeTime> getFreeTimesByBusinessAndCheckWorkingTime(LocalDateTime begin, LocalDateTime finish, BaseBusinessDto business, UUID workerId) {
        WorkTimeDto workTime = getWorkTimeByBusiness(begin, business);
        checkBusinessTimeWorking(workTime, begin, finish);
        return getFreeTimesByBusiness(business.getId(), workerId, begin.toLocalDate().atTime(workTime.getFrom()), begin.toLocalDate().atTime(workTime.getTo()), begin);
    }

    private List<RecordFreeTime> getFreeTimesByBusiness(UUID businessId, UUID workerId, LocalDateTime startTimeWork, LocalDateTime endTimeWork, LocalDateTime beginRecord) {
        List<RecordFreeTime> result = new ArrayList();
        List<BaseRecordEntity> businessRecords = baseRecordRepository.findByBusinessIdAndStatusRecordAndBeginBetween(businessId, StatusRecord.CREATED, startTimeWork, endTimeWork);
        List<WorkerDto> workers = workerService.getByBusinessId(businessId, false);
        if (CollectionUtils.isEmpty(workers) || workers.stream().allMatch(m -> m.getWorkingSpaceId() == null)) {
            throw new ClientException(BUSINESS_DOES_NOT_ANY_WORKER);
        }
        workers = workers.stream().filter(f -> f.getWorkingSpaceId() != null).collect(Collectors.toList());
        if (workerId != null) {
            workers = workers.stream().filter(f -> f.getId().equals(workerId)).collect(Collectors.toList());
        }
        workers.forEach(worker -> {
            WorkTimeDto workTime = getWorkTimeByWorker(worker, beginRecord);
            if (worker.getId().equals(workerId)) {
                checkWorkerTimeWorking(workTime, beginRecord);
            }
            if (workTime != null) {
                LocalDateTime beginWorkerTime = beginRecord.toLocalDate().atTime(workTime.getFrom());
                LocalDateTime endWorkerTime = beginRecord.toLocalDate().atTime(workTime.getTo());
                if (CollectionUtils.isNotEmpty(businessRecords)) {
                    List<BaseRecordEntity> records = businessRecords.stream().filter(f -> f.getWorkerId().equals(worker.getId())).collect(Collectors.toList());
                    if (CollectionUtils.isNotEmpty(records)) {
                        records.sort(Comparator.comparing(BaseRecordEntity::getBegin));
                        for (int i = 0; i < records.size(); i++) {
                            if (i == 0) {
                                result.add(new RecordFreeTime(worker.getWorkingSpaceId(), worker.getId(), beginWorkerTime, records.get(i).getBegin()));
                            }
                            if (i > 0 && records.size() > 1) {
                                result.add(new RecordFreeTime(worker.getWorkingSpaceId(), worker.getId(), records.get(i - 1).getFinish(), records.get(i).getBegin()));
                            }
                            if (i == records.size() - 1) {
                                result.add(new RecordFreeTime(worker.getWorkingSpaceId(), worker.getId(), records.get(i).getFinish(), endWorkerTime));
                            }
                        }
                    } else {
                        if (ObjectUtils.allNotNull(beginWorkerTime, endWorkerTime)) {
                            result.add(new RecordFreeTime(worker.getWorkingSpaceId(), worker.getId(), beginWorkerTime, endWorkerTime));
                        }
                    }
                } else {
                    if (ObjectUtils.allNotNull(beginWorkerTime, endWorkerTime)) {
                        result.add(new RecordFreeTime(worker.getWorkingSpaceId(), worker.getId(), beginWorkerTime, endWorkerTime));
                    }
                }
            }
        });
        return result;
    }


    private WorkTimeDto getWorkTimeByWorker(WorkerDto worker, LocalDateTime beginRecord) {
        WorkTimeDto result = null;
        if (worker != null && CollectionUtils.isNotEmpty(worker.getWorkTimes())) {
            result = worker.getWorkTimes().stream()
                    .filter(wt -> wt.getDayOfWeek().equals(beginRecord.getDayOfWeek())).filter(w -> w.getIsWork()).findFirst().orElse(null);
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
                        record.setServices(recordServices.get(record.getId()));
                        if (record.getServices() == null) {
                            record.setServices(new ArrayList<>());
                        }

                    });
                }
            }
        }
        return records;
    }

    private List<BaseRecordDto> setServicePriceIds(List<BaseRecordDto> records) {
        if (CollectionUtils.isNotEmpty(records)) {
            Map<UUID, List<UUID>> mapIds = recordServiceService.getServicePriceIds(records.stream().map(BaseRecordDto::getId).collect(Collectors.toList()));
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

    private Pageable getPageable(BusinessRecordSearchPageableDto search) {
        Integer page = 0;
        Integer size = 10;
        Sort sort = null;
        if (search != null) {
            if (search.getPage() != null) page = search.getPage();
            if (search.getSize() != null) size = search.getSize();
            if (search.getSortField() != null) {
                sort = Sort.by((search.getSortDirection() != null) ? search.getSortDirection() : Sort.Direction.ASC, search.getSortField().toString());
            }
        }
        if (sort != null) {
            return PageRequest.of(page, size, sort);
        } else {
            return PageRequest.of(page, size);
        }
    }

    private void setSearch(ClientRecordSearchDto search) {
        if (search == null) {
            search = new ClientRecordSearchDto();
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

    private UUID getWorkingSpaceIfWorkerOrCheckPermissionToViewAll(UUID businessId) {
        SecurityUtil.checkUserByBanStatus();
        WorkerDto worker = workerService.findByUserIdAndBusinessId(SecurityUtil.getUserId(), businessId);
        UUID result = null;
        if (!businessPermissionFacade.isHavePermissionByBusiness(businessId, BusinessPermission.VIEW_ALL_RECORD) &&
                ((worker == null) || ((result = worker.getWorkingSpaceId()) == null))) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
        return result;
    }
}
