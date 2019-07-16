package com.gliesereum.account.facade.notification.impl;

import com.gliesereum.account.facade.notification.SystemNotificationFacade;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeUserDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.notification.notification.SystemNotificationDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private FanoutExchange updateAuthInfoExchange;

    @Autowired
    private FanoutExchange updateClientInfoExchange;

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

    @Override
    @Async
    public void sendUpdateAuthInfoNotification(List<AuthDto> authInfos) {
        if (CollectionUtils.isNotEmpty(authInfos)) {
            SystemNotificationDto<List<AuthDto>> notification = new SystemNotificationDto<>();
            notification.setObject(authInfos);
            notification.setObjectId(null);
            rabbitTemplate.convertAndSend(updateAuthInfoExchange.getName(), "", notification);
        }
    }

    @Override
    @Async
    public void updateClientInfo(UserDto user) {
        if(user != null){
            SystemNotificationDto<UserDto> notification = new SystemNotificationDto<>();
            notification.setObjectId(user.getId());
            notification.setObject(user);
            rabbitTemplate.convertAndSend(updateClientInfoExchange.getName(), "", notification);
        }
    }
}
