package com.gliesereum.lendinggallery.facade.notification;

import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferDto;

public interface OfferNotificationFacade {
	
	void createOfferNotification(InvestorOfferDto offer);
}
