package com.gliesereum.account.facade.notification.impl;

import com.gliesereum.account.facade.notification.SystemNotificationFacade;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeUserDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.notification.notification.SystemNotificationDto;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class SystemNotificationFacadeImpl implements SystemNotificationFacade {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private FanoutExchange corporationDeleteExchange;

    @Autowired
    private FanoutExchange signupWithCodeExchange;

    @Async
    @Override
    public void sendCorporationDelete(CorporationDto corporation) {
        if ((corporation != null) && (corporation.getId() != null)) {
            SystemNotificationDto<CorporationDto> notification = new SystemNotificationDto<>();
            notification.setObjectId(corporation.getId());
            notification.setObject(corporation);
            rabbitTemplate.convertAndSend(corporationDeleteExchange.getName(), "", notification);
        }
    }

    @Override
    @Async
    public void sendSignUpWithCodeNotification(ReferralCodeUserDto referralCodeUser) {
        if (referralCodeUser != null) {
            SystemNotificationDto<ReferralCodeUserDto> notification = new SystemNotificationDto<>();
            notification.setObject(referralCodeUser);
            notification.setObjectId(referralCodeUser.getId());
            rabbitTemplate.convertAndSend(signupWithCodeExchange.getName(), "", notification);
        }
    }
}
