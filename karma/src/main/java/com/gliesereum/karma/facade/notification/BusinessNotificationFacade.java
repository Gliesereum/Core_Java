package com.gliesereum.karma.facade.notification;

import com.gliesereum.share.common.model.dto.karma.business.AbstractBusinessDto;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface BusinessNotificationFacade {

    void newBusinessNotification(AbstractBusinessDto business);
}
