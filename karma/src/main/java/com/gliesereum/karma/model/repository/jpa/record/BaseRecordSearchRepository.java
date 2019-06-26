package com.gliesereum.karma.model.repository.jpa.record;

import com.gliesereum.karma.model.entity.record.BaseRecordEntity;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.RecordsSearchDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BaseRecordSearchRepository {

    List<BaseRecordEntity> getByTimeBetween(LocalDateTime from, Integer minutesFrom, Integer minutesTo, StatusRecord status, boolean notificationSend);

    List<BaseRecordEntity> getRecordsBySearchDto(RecordsSearchDto search);
}
