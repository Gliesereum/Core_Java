package com.gliesereum.karma.facade.notification;

import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.karma.record.RecordNotificationDto;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface RecordNotificationFacade {

    void recordBusinessNotification(BaseRecordDto record);
    
    void recordCreateBusinessNotification(RecordNotificationDto recordNotificationDto);

    void recordClientNotification(BaseRecordDto record);

    void recordRemindNotification(BaseRecordDto record);
}
