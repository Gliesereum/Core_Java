package com.gliesereum.karma.service.analytics.impl;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.karma.model.repository.jpa.record.BaseRecordRepository;
import com.gliesereum.karma.service.analytics.AnalyticsService;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.business.WorkingSpaceService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.karma.service.service.PackageService;
import com.gliesereum.karma.service.service.ServicePriceService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticDto;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticFilterDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkerDto;
import com.gliesereum.share.common.model.dto.karma.business.LiteWorkingSpaceDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.service.LitePackageDto;
import com.gliesereum.share.common.model.dto.karma.service.LiteServicePriceDto;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.BODY_INVALID;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.*;

/**
 * @author vitalij
 * @version 1.0
 */
@Slf4j
@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private BaseRecordRepository baseRecordRepository;

    @Autowired
    private WorkerService workerService;

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private BaseRecordService baseRecordService;

    @Autowired
    private ServicePriceService servicePriceService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private WorkingSpaceService workingSpaceService;

    @Override
    public AnalyticDto getAnalyticByFilter(AnalyticFilterDto filter) {
        LocalDateTime start = LocalDateTime.now();
        checkFilter(filter);
        AnalyticDto result = new AnalyticDto();

        List<BaseRecordEntity> entities = baseRecordRepository.getRecordsByFilter(filter);

        Map<UUID, Set<BaseRecordDto>> services = new HashMap<>();
        Map<UUID, Set<BaseRecordDto>> packages = new HashMap<>();
        Map<UUID, Set<BaseRecordDto>> workers = new HashMap<>();
        Map<UUID, Set<BaseRecordDto>> spaces = new HashMap<>();

        if (CollectionUtils.isNotEmpty(entities)) {

            List<BaseRecordDto> dtos = baseRecordService.convertListEntityToDto(entities);

            dtos.forEach(record -> {

                if (CollectionUtils.isNotEmpty(record.getServices())) {
                    record.getServices().forEach(s -> {
                        if (services.containsKey(s.getId())) {
                            services.get(s.getId()).add(record);
                        } else {
                            Set<BaseRecordDto> records = getNewRecordTreeSet();
                            records.add(record);
                            services.put(s.getId(), records);
                        }
                    });
                }

                if (record.getPackageId() != null) {
                    if (packages.containsKey(record.getPackageId())) {
                        packages.get(record.getPackageId()).add(record);
                    } else {
                        Set<BaseRecordDto> records = getNewRecordTreeSet();
                        records.add(record);
                        packages.put(record.getPackageId(), records);
                    }
                }

                if (record.getWorkerId() != null) {
                    if (workers.containsKey(record.getWorkerId())) {
                        workers.get(record.getWorkerId()).add(record);
                    } else {
                        Set<BaseRecordDto> records = getNewRecordTreeSet();
                        records.add(record);
                        workers.put(record.getWorkerId(), records);
                    }
                }

                if (record.getWorkingSpaceId() != null) {
                    if (spaces.containsKey(record.getWorkingSpaceId())) {
                        spaces.get(record.getWorkingSpaceId()).add(record);
                    } else {
                        Set<BaseRecordDto> records = getNewRecordTreeSet();
                        records.add(record);
                        spaces.put(record.getWorkingSpaceId(), records);
                    }
                }
            });
        }

        List<LitePackageDto> businessPackages = packageService.getLitePackageByBusinessId(filter.getBusinessId());
        if (CollectionUtils.isNotEmpty(businessPackages)) {
            businessPackages.forEach(f -> {
                Set<BaseRecordDto> record;
                result.getPackages().put(f.getName(), (record = packages.get(f.getId())) != null ? record : new HashSet<>());
            });
            result.setPackages(sortMap(result.getPackages()));
        }

        List<LiteServicePriceDto> businessServices = servicePriceService.getLiteServicePriceByBusinessId(filter.getBusinessId());
        if (CollectionUtils.isNotEmpty(businessServices)) {
            businessServices.forEach(f -> {
                Set<BaseRecordDto> record;
                result.getServices().put(f.getName(), (record = services.get(f.getId())) != null ? record : new HashSet<>());
            });
            result.setServices(sortMap(result.getServices()));
        }

        List<LiteWorkingSpaceDto> businessWorkingSpace = workingSpaceService.getLiteWorkingSpaceByBusinessId(filter.getBusinessId());
        if (CollectionUtils.isNotEmpty(businessWorkingSpace)) {
            businessWorkingSpace.forEach(f -> {
                Set<BaseRecordDto> record;
                result.getWorkingSpaces().put(f.getIndexNumber().toString(), (record = spaces.get(f.getId())) != null ? record : new HashSet<>());
            });
            result.setWorkingSpaces(sortMap(result.getWorkingSpaces()));
        }

        List<LiteWorkerDto> businessWorkers = workerService.getLiteWorkerByBusinessId(filter.getBusinessId());
        if (CollectionUtils.isNotEmpty(businessWorkers)) {
            businessWorkers.forEach(f -> {
                if (f.getUser() != null) {
                    Set<BaseRecordDto> record;
                    result.getWorkers().put(f.getUser().getFirstName().concat(" ").concat(f.getUser().getLastName()), (record = workers.get(f.getId())) != null ? record : new HashSet<>());
                }
            });
            result.setWorkers(sortMap(result.getWorkers()));
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ " + Duration.between(start, LocalDateTime.now()));
        System.out.println("+++++++++++++++ ");
        return result;
    }

    private void checkFilter(AnalyticFilterDto filter) {
        if (filter == null) {
            throw new ClientException(BODY_INVALID);
        }
        SecurityUtil.checkUserByBanStatus();
        if (filter.getBusinessId() == null) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        if (!baseBusinessService.currentUserHavePermissionToActionInBusinessLikeOwner(filter.getBusinessId())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
        }
        if (filter.getFrom() != null && filter.getTo() != null && filter.getFrom().isAfter(filter.getTo())) {
            throw new ClientException(TIME_IS_NOT_CORRECT);
        }
    }

    private Map<String, Set<BaseRecordDto>> sortMap(Map<String, Set<BaseRecordDto>> map) {
        if (!map.isEmpty()) {
            return map.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Set::size)))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        }
        return map;
    }

    private Set<BaseRecordDto> getNewRecordTreeSet() {
        return new TreeSet<>(new Comparator<BaseRecordDto>() {
            @Override
            public int compare(BaseRecordDto r1, BaseRecordDto r2) {
                return r1.getBegin().compareTo(r2.getBegin());
            }
        });
    }
}
