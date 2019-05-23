package com.gliesereum.karma.mq;

import com.gliesereum.karma.service.business.BaseBusinessService;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.notification.notification.NotificationDto;
import lombok.extern.slf4j.Slf4j;
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

    @RabbitListener(bindings = @QueueBinding(value = @Queue, exchange = @Exchange(
            value = "${system-notification.corporation-delete.exchange-name}",
            ignoreDeclarationExceptions = "true", type = ExchangeTypes.FANOUT)))
    public void receiveCorporationDeleteNotification(NotificationDto<CorporationDto> corporationNotification) {
        try {
            if ((corporationNotification != null) && (corporationNotification.getObjectId() != null)) {
                baseBusinessService.deleteByCorporationId(corporationNotification.getObjectId());
            }
        } catch (Exception e) {
            log.warn("Error while process corporation delete notification", e);
        }
    }
}
