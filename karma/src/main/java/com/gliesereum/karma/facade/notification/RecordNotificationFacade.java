package com.gliesereum.karma.facade.notification;

import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface RecordNotificationFacade {

    void recordCreateNotification(BaseRecordDto record);

    void recordUpdateNotification(BaseRecordDto record);

    void recordRemindNotification(BaseRecordDto record);
}
