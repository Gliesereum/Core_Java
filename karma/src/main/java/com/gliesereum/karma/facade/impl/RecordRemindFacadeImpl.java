package com.gliesereum.karma.facade.impl;

import com.gliesereum.karma.facade.notification.RecordNotificationFacade;
import com.gliesereum.karma.facade.RecordRemindFacade;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.share.common.model.dto.karma.business.LiteBusinessDto;
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
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class RecordRemindFacadeImpl implements RecordRemindFacade {

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private BaseRecordService baseRecordService;

    @Autowired
    private RecordNotificationFacade recordNotificationFacade;

    @Scheduled(fixedRate = 9 * 60 * 1000)
    public void recordRemind() {
        List<LiteBusinessDto> businessList = baseBusinessService.getAllLite();
        if (CollectionUtils.isNotEmpty(businessList)) {
            for (LiteBusinessDto business : businessList) {
                LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
                LocalDateTime currentTimeOnBusiness = now.plusMinutes(business.getTimeZone());
                LocalDateTime fromTime = currentTimeOnBusiness.plusMinutes(25);
                LocalDateTime toTime = currentTimeOnBusiness.plusMinutes(35);

                List<BaseRecordDto> records = baseRecordService.getByBusinessIdAndStatusRecordNotificationSend(business.getId(), StatusRecord.CREATED, fromTime, toTime, false);

                if (CollectionUtils.isNotEmpty(records)) {
                    records.forEach(record -> {
                        recordNotificationFacade.recordRemindNotification(record);
                        baseRecordService.setNotificationSend(record.getId());
                    });
                }
            }

        }
    }
}
