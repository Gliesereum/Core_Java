package com.gliesereum.karma.aspect.config;

import com.gliesereum.karma.facade.notification.RecordNotificationFacade;
import com.gliesereum.share.common.model.dto.karma.enumerated.StatusRecord;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import com.gliesereum.share.common.util.SecurityUtil;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Component
@Aspect
public class RecordChangeAspect {

    @Autowired
    private RecordNotificationFacade recordNotificationFacade;

    @AfterReturning(pointcut = "@annotation(com.gliesereum.karma.aspect.annotation.RecordUpdate)", returning = "retVal")
    public void recordUpdateEvent(BaseRecordDto retVal) {
        if ((retVal != null) && (retVal.getClientId() != null)) {
            StatusRecord statusRecord = retVal.getStatusRecord();
            if ((statusRecord != null) && statusRecord.equals(StatusRecord.CANCELED) && retVal.getClientId().equals(SecurityUtil.getUserId())) {
                recordNotificationFacade.recordBusinessNotification(retVal);
            } else {
                recordNotificationFacade.recordClientNotification(retVal);
            }
        }

    }

    @AfterReturning(pointcut = "@annotation(com.gliesereum.karma.aspect.annotation.RecordCreate)", returning = "retVal")
    public void recordCreateEvent(BaseRecordDto retVal) {
        if (retVal.getClientId() != null) {
            if (retVal.getClientId().equals(SecurityUtil.getUserId())) {
                recordNotificationFacade.recordBusinessNotification(retVal);
            } else {
                recordNotificationFacade.recordClientNotification(retVal);
            }
        }
    }

}
