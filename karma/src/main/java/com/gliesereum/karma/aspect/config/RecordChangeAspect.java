package com.gliesereum.karma.aspect.config;

import com.gliesereum.karma.facade.RecordNotificationFacade;
import com.gliesereum.share.common.model.dto.karma.record.AbstractRecordDto;
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
    public void recordUpdateEvent(AbstractRecordDto retVal) {
        recordNotificationFacade.recordUpdateNotification(retVal);
    }

    @AfterReturning(pointcut = "@annotation(com.gliesereum.karma.aspect.annotation.RecordCreate)", returning = "retVal")
    public void recordCreateEvent(AbstractRecordDto retVal) {
        recordNotificationFacade.recordCreateNotification(retVal);
    }

}
