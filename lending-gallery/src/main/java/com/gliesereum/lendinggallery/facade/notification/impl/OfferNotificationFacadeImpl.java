package com.gliesereum.lendinggallery.facade.notification.impl;

import com.gliesereum.lendinggallery.facade.notification.OfferNotificationFacade;
import com.gliesereum.lendinggallery.service.advisor.AdvisorService;
import com.gliesereum.share.common.model.dto.lendinggallery.offer.InvestorOfferDto;
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OfferNotificationFacadeImpl implements OfferNotificationFacade {
	
	@Value("${notification.lg.create-offer.queueName}")
	private String createOfferQueue;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private AdvisorService advisorService;
	
	@Override
	public void createOfferNotification(InvestorOfferDto offer) {
		if (offer != null) {
			NotificationDto<InvestorOfferDto> notification = new NotificationDto<>();
			notification.setData(offer);
			notification.setSubscribeDestination(SubscribeDestination.LG_CREATE_OFFER);
			notification.setObjectId(offer.getId());
			notification.setUserIds(advisorService.getUserIdsByArtBondId(offer.getArtBondId()));
			rabbitTemplate.convertAndSend(createOfferQueue, notification);
		}
	}
}
