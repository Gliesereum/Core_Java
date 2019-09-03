package com.gliesereum.karma.facade.notification.impl;

import com.gliesereum.karma.facade.notification.RecordRemindFacade;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusProcess;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 */

@Service
public class RecordUpdateFacadeImpl implements RecordRemindFacade {

    @Autowired
    private BaseRecordService baseRecordService;

    @Scheduled(fixedRate = 5 * 60 * 1000)
    public void recordUpdateExpired() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        now = now.minusMinutes(4L);
        List<BaseRecordDto> records = baseRecordService.getByFinishTimeInPast(now, StatusProcess.WAITING);
        if (CollectionUtils.isNotEmpty(records)) {
            records.forEach(record -> {
                record.setStatusProcess(StatusProcess.EXPIRED);
                record.setStatusRecord(StatusRecord.EXPIRED);
            });
            baseRecordService.update(records);
        }
    }
}
