package com.gliesereum.karma.facade.impl;

import com.gliesereum.karma.facade.RecordNotificationFacade;
import com.gliesereum.karma.service.car.CarService;
import com.gliesereum.karma.service.record.BaseRecordService;
import com.gliesereum.share.common.model.dto.karma.car.CarDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.ServiceType;
import com.gliesereum.share.common.model.dto.karma.record.AbstractRecordDto;
import com.gliesereum.share.common.model.dto.karma.record.BaseRecordDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class RecordNotificationFacadeImpl implements RecordNotificationFacade {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${notification.topicName}")
    private String notificationTopic;

    @Value("${notification.businessRecordRouting}")
    private String businessRecordRouting;

    @Value("${notification.userRecordRouting}")
    private String userRecordRouting;

    @Autowired
    private CarService carService;

    @Autowired
    private BaseRecordService baseRecordService;

    @Override
    @Async
    public void recordCreateNotification(AbstractRecordDto record) {
        if (record != null) {
            BaseRecordDto fullModel = baseRecordService.getFullModelById(record.getId());
            if (fullModel != null) {
                rabbitTemplate.convertAndSend(notificationTopic, routingKey(businessRecordRouting, record.getBusinessId()), fullModel);
            }
        }
    }

    @Override
    @Async
    public void recordUpdateNotification(AbstractRecordDto record) {
        if (record != null) {
            BaseRecordDto fullModel = baseRecordService.getFullModelById(record.getId());
            if (fullModel != null) {
                UUID id = null;
                if (record.getServiceType().equals(ServiceType.CAR_WASH)) {
                    CarDto car = carService.getById(record.getTargetId());
                    id = car.getUserId();
                }
                if (id != null) {
                    rabbitTemplate.convertAndSend(notificationTopic, routingKey(userRecordRouting, id), fullModel);
                }
            }
        }
    }

    private String routingKey(String routing, UUID id) {
        return routing + '.' + id.toString();
    }
}
