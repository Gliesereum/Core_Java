package com.gliesereum.notification.service.notification;

import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface NotificationService {

    void processRecordNotification(NotificationDto<BaseRecordDto> recordNotification);
}
