package com.gliesereum.karma.service.analytics.impl;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.karma.model.repository.jpa.record.BaseRecordRepository;
import com.gliesereum.karma.service.analytics.AnalyticsService;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.business.WorkerService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticFilterDto;
import com.gliesereum.share.common.model.dto.karma.analytics.AnalyticRatingDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.BUSINESS_ID_EMPTY;
import static com.gliesereum.share.common.exception.messages.KarmaExceptionMessage.DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS;

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

    @Override
    public List<BaseRecordDto> getByAnalyticFilter(AnalyticFilterDto analyticFilter) {
        checkAndSetFilter(analyticFilter);
        return setByFilterRecord(analyticFilter);
    }

    @Override
    public AnalyticRatingDto getPackageAndServiceRating(AnalyticFilterDto analyticFilter) {
        return null;
    }


    private void checkAndSetFilter(AnalyticFilterDto analyticFilter) {
        SecurityUtil.checkUserByBanStatus();
        if (CollectionUtils.isEmpty(analyticFilter.getBusinessIds())) {
            throw new ClientException(BUSINESS_ID_EMPTY);
        }
        if (CollectionUtils.isNotEmpty(analyticFilter.getBusinessIds())) {
            analyticFilter.getBusinessIds().forEach(f -> {
                if (!baseBusinessService.currentUserHavePermissionToActionInBusinessLikeOwner(f)) {
                    throw new ClientException(DONT_HAVE_PERMISSION_TO_ACTION_BUSINESS);
                }
            });
        }
        if (analyticFilter.getFrom() == null) {
            analyticFilter.setFrom(LocalDateTime.now(ZoneOffset.UTC).toLocalDate().atStartOfDay());
        }
        if (CollectionUtils.isEmpty(analyticFilter.getStatus())) {
            analyticFilter.setStatus(Arrays.asList(StatusRecord.values()));
        }
        if (analyticFilter.getTo() == null ||
                analyticFilter.getFrom().isAfter(analyticFilter.getTo())) {
            analyticFilter.setTo(analyticFilter.getFrom().toLocalDate().atStartOfDay().plusDays(1).minusSeconds(1));
        }
    }

    private List<BaseRecordDto> setByFilterRecord(AnalyticFilterDto filter) {
        List<BaseRecordEntity> entitiesFilter = new ArrayList<>();
        List<BaseRecordEntity> entities =
                baseRecordRepository.findByStatusRecordInAndBusinessIdInAndBeginBetweenOrderByBegin(
                        filter.getStatus(), filter.getBusinessIds(), filter.getFrom(), filter.getTo());
        if (CollectionUtils.isNotEmpty(entities)) {
            entities.forEach(f -> {

                if (CollectionUtils.isNotEmpty(filter.getPackageIds()) &&
                        f.getPackageId() != null &&
                        filter.getPackageIds().contains(f.getPackageId())) {
                    entitiesFilter.add(f);
                }

                if (CollectionUtils.isNotEmpty(filter.getServicePriceIds()) &&
                        CollectionUtils.isNotEmpty(f.getServices()) &&
                        !Collections.disjoint(f.getServices().stream().map(m -> m.getId()).collect(Collectors.toList()), filter.getServicePriceIds())) {
                    entitiesFilter.add(f);
                }

                if (filter.getTargetId() != null && f.getTargetId().equals(filter.getTargetId())) {
                    entitiesFilter.add(f);
                }

                if (filter.getClientId() != null && f.getClientId().equals(filter.getClientId())) {
                    entitiesFilter.add(f);
                }

                if (filter.getWorkerId() != null &&
                        f.getWorkingSpaceId() != null &&
                        workerService.getByWorkingSpaceId(f.getWorkingSpaceId()).stream().anyMatch(w -> w.getUserId().equals(filter.getWorkerId()))) {
                    entitiesFilter.add(f);
                }
            });
        }
        List<BaseRecordDto> result = baseRecordService.convertListEntityToDto(entitiesFilter);
        if (CollectionUtils.isNotEmpty(result)) {
            baseRecordService.setFullModelRecord(result);
        }
        return result;
    }

}
