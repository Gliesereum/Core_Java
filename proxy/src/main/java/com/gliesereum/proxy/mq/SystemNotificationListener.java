package com.gliesereum.proxy.mq;

import com.gliesereum.proxy.facade.CacheFacade;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.notification.notification.SystemNotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Slf4j
@Component
public class SystemNotificationListener {

    @Autowired
    private CacheFacade cacheFacade;

    @RabbitListener(bindings = @QueueBinding(value = @Queue, exchange = @Exchange(
            value = "${system-notification.update-auth-info.exchange-name}",
            ignoreDeclarationExceptions = "true", type = ExchangeTypes.FANOUT)))
    public void receiveUpdateAuthInfoNotification(SystemNotificationDto<List<AuthDto>> updateAuthInfoNotification) {
        try {
            if ((updateAuthInfoNotification != null) && CollectionUtils.isNotEmpty(updateAuthInfoNotification.getObject())) {
                cacheFacade.updateAuthCache(updateAuthInfoNotification.getObject());
            }
        } catch (Exception e) {
            log.warn("Error while process update auth info notification", e);
        }
    }
}
