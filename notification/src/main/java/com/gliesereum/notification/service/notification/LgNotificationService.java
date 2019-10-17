package com.gliesereum.notification.service.notification;

import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferDto;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;

public interface LgNotificationService {
	
	void processCreateOfferNotification(NotificationDto<InvestorOfferDto> createOfferNotification);
}
