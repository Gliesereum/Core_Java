package com.gliesereum.notification.service.device.impl;

import com.gliesereum.notification.model.entity.device.UserDeviceEntity;
import com.gliesereum.notification.model.repository.jpa.device.UserDeviceRepository;
import com.gliesereum.notification.service.device.UserDeviceService;
import com.gliesereum.notification.service.firebase.FirebaseService;
import com.gliesereum.notification.service.subscribe.UserSubscribeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceDto;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceRegistrationDto;
import com.gliesereum.share.common.model.dto.notification.subscribe.UserSubscribeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.NotificationExceptionMessage.REGISTRATION_TOKEN_EXIST;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class UserDeviceServiceImpl extends DefaultServiceImpl<UserDeviceDto, UserDeviceEntity> implements UserDeviceService {

    private static final Class<UserDeviceDto> DTO_CLASS = UserDeviceDto.class;

    private static final Class<UserDeviceEntity> ENTITY_CLASS = UserDeviceEntity.class;

    private final UserDeviceRepository userDeviceRepository;

    @Autowired
    private UserSubscribeService userSubscribeService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    public UserDeviceServiceImpl(UserDeviceRepository userDeviceRepository,
                                 DefaultConverter defaultConverter) {
        super(userDeviceRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.userDeviceRepository = userDeviceRepository;
    }

    @Override
    public UserDeviceDto registerDevice(UserDeviceRegistrationDto userDeviceRegistration) {
        UserDeviceDto result = null;
        if (userDeviceRegistration != null) {
            if (userDeviceRepository.existsByFirebaseRegistrationToken(userDeviceRegistration.getFirebaseRegistrationToken())) {
                throw new ClientException(REGISTRATION_TOKEN_EXIST);
            }
            UserDeviceEntity entity = converter.convert(userDeviceRegistration, entityClass);
            entity = userDeviceRepository.saveAndFlush(entity);
            UUID userDeviceId = entity.getId();
            if (CollectionUtils.isNotEmpty(userDeviceRegistration.getSubscribes())) {
                List<UserSubscribeDto> subscribes = userDeviceRegistration.getSubscribes().stream()
                        .peek(i -> i.setUserDeviceId(userDeviceId))
                        .collect(Collectors.toList());
                List<UserSubscribeDto> subscribe = userSubscribeService.subscribe(subscribes, entity.getUserId());
                if (CollectionUtils.isNotEmpty(subscribe)) {
                    subscribe.forEach(i -> {
                        firebaseService.subscribeToTopic(userDeviceRegistration.getFirebaseRegistrationToken(), i.getSubscribeDestination().toString(), i.getObjectId());
                    });
                }
            }
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }
}