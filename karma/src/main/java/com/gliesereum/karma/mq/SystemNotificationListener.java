package com.gliesereum.karma.mq;

import com.gliesereum.karma.facade.bonus.BonusScoreFacade;
import com.gliesereum.karma.facade.client.ClientFacade;
import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.karma.service.es.ClientEsService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeUserDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.notification.notification.SystemNotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Slf4j
@Component
public class SystemNotificationListener {

    @Autowired
    private BaseBusinessService baseBusinessService;

    @Autowired
    private BonusScoreFacade bonusScoreFacade;

    @Autowired
    private ClientFacade clientFacade;

    @RabbitListener(bindings = @QueueBinding(value = @Queue, exchange = @Exchange(
            value = "${system-notification.corporation-delete.exchange-name}",
            ignoreDeclarationExceptions = "true", type = ExchangeTypes.FANOUT)))
    public void receiveCorporationDeleteNotification(SystemNotificationDto<CorporationDto> corporationNotification) {
        try {
            if ((corporationNotification != null) && (corporationNotification.getObjectId() != null)) {
                baseBusinessService.deleteByCorporationId(corporationNotification.getObjectId());
            }
        } catch (Exception e) {
            log.warn("Error while process corporation delete notification", e);
        }
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue("${system-notification.signup-with-code.queue-name}"), exchange = @Exchange(
            value = "${system-notification.signup-with-code.exchange-name}",
            ignoreDeclarationExceptions = "true", type = ExchangeTypes.FANOUT)))
    public void receiveSignupWithCodeNotification(SystemNotificationDto<ReferralCodeUserDto> signupWithCodeNotification) {
        try {
            if ((signupWithCodeNotification != null) && (signupWithCodeNotification.getObject() != null)) {
                bonusScoreFacade.addScoreBySignupWithCode(signupWithCodeNotification.getObject());
            }
        } catch (Exception e) {
            log.warn("Error while process signup with code notification", e);
        }
    }

    @RabbitListener(bindings = @QueueBinding(value = @Queue("${system-notification.update-client-info.queue-name}"), exchange = @Exchange(
            value = "${system-notification.update-client-info.exchange-name}",
            ignoreDeclarationExceptions = "true", type = ExchangeTypes.FANOUT)))
    public void updateClientInfo(SystemNotificationDto<UserDto> userSystemNotification) {
        try {
            if ((userSystemNotification != null) && (userSystemNotification.getObject() != null)) {
                clientFacade.updateClientInfo(userSystemNotification.getObject());
            }
        } catch (Exception e) {
            log.warn("Error while process update client notification", e);
        }
    }

}
