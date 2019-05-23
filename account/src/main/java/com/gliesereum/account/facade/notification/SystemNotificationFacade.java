package com.gliesereum.account.facade.notification;

import com.gliesereum.share.common.model.dto.account.user.CorporationDto;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface SystemNotificationFacade {

    void sendCorporationDelete(CorporationDto corporation);
}
