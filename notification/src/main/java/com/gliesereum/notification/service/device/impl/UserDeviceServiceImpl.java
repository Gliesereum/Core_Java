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
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.dto.notification.subscribe.UserSubscribeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Transactional
    public void removeDevice(String registrationToken) {
        UserDeviceEntity userDevice = userDeviceRepository.findByFirebaseRegistrationToken(registrationToken);
        if (userDevice != null) {
            userSubscribeService.unsubscribeAll(userDevice.getId(), userDevice.getFirebaseRegistrationToken());
            userDeviceRepository.delete(userDevice);
        }
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
                List<UserSubscribeDto> subscribe = userSubscribeService.subscribe(subscribes, entity.getUserId(), entity.getFirebaseRegistrationToken());
            }
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }

    @Override
    public List<UserDeviceDto> getByUserId(UUID userId) {
        List<UserDeviceDto> userDevices = null;
        if (userId != null) {
            List<UserDeviceEntity> entities = userDeviceRepository.findAllByUserId(userId);
            userDevices = converter.convert(entities, dtoClass);
        }
        return userDevices;
    }

    @Override
    public List<UserDeviceDto> getByUserIds(List<UUID> userIds) {
        List<UserDeviceDto> userDevices = null;
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<UserDeviceEntity> entities = userDeviceRepository.findAllByUserIdInAndNotificationEnableTrue(userIds);
            userDevices = converter.convert(entities, dtoClass);
        }
        return userDevices;
    }

    @Override
    public List<UserDeviceDto> getByUserIdsAndSubscribeExist(List<UUID> userIds, SubscribeDestination subscribeDestination) {
        List<UserDeviceDto> userDevices = null;
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<UserDeviceEntity> entities = userDeviceRepository.findAllByUserIdInAndNotificationEnableTrue(userIds);
            if (CollectionUtils.isNotEmpty(entities)) {
                Map<UUID, UserDeviceEntity> entityMap = entities.stream().collect(Collectors.toMap(UserDeviceEntity::getId, i -> i));
                List<UserSubscribeDto> subscribes = userSubscribeService.getAllByUserDeviceIdAndSubscribeDestination(new ArrayList<>(entityMap.keySet()), subscribeDestination);
                if (CollectionUtils.isNotEmpty(subscribes)) {
                    userDevices = new ArrayList<>();
                    for (UserSubscribeDto subscribe : subscribes) {
                        UserDeviceEntity entity = entityMap.get(subscribe.getUserDeviceId());
                        userDevices.add(converter.convert(entity, dtoClass));
                    }
                }
            }
        }
        return userDevices;
    }

    @Override
    public UserDeviceDto getByRegistrationToken(String registrationToken) {
        UserDeviceDto result = null;
        if (StringUtils.isNotEmpty(registrationToken)) {
            UserDeviceEntity entity = userDeviceRepository.findByFirebaseRegistrationToken(registrationToken);
            result = converter.convert(entity, dtoClass);
        }
        return result;
    }
}