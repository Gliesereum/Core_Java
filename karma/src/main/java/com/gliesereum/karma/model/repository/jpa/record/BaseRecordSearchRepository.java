package com.gliesereum.karma.model.repository.jpa.record;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.RecordPaymentInfoDto;
import com.gliesereum.share.common.model.dto.karma.record.search.BusinessRecordSearchDto;
import com.gliesereum.share.common.model.dto.karma.record.search.BusinessRecordSearchPageableDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BaseRecordSearchRepository {

    List<BaseRecordEntity> getByTimeBetween(LocalDateTime from, Integer minutesFrom, Integer minutesTo, StatusRecord status, boolean notificationSend);

    List<BaseRecordEntity> getByFinishTimeInPast(LocalDateTime from, StatusProcess status);

    long countBusyWorker(LocalDateTime time, StatusRecord status);

    Page<BaseRecordEntity> getRecordsBySearchDto(BusinessRecordSearchPageableDto search, Pageable pageable);

    RecordPaymentInfoDto getPaymentInfoBySearch(BusinessRecordSearchDto search);
}
