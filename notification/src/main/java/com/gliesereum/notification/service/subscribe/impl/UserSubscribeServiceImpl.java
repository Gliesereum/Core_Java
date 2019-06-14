package com.gliesereum.notification.service.subscribe.impl;

import com.gliesereum.notification.model.entity.subscribe.UserSubscribeEntity;
import com.gliesereum.notification.model.repository.jpa.subscribe.UserSubscribeRepository;
import com.gliesereum.notification.service.device.UserDeviceService;
import com.gliesereum.notification.service.firebase.FirebaseService;
import com.gliesereum.notification.service.subscribe.UserSubscribeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.karma.KarmaExchangeService;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceDto;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceRegistrationDto;
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.dto.notification.subscribe.UserSubscribeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.NotificationExceptionMessage.REGISTRATION_TOKEN_NOT_EXIST;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Slf4j
@Service
public class UserSubscribeServiceImpl extends DefaultServiceImpl<UserSubscribeDto, UserSubscribeEntity> implements UserSubscribeService {

    private static final Class<UserSubscribeDto> DTO_CLASS = UserSubscribeDto.class;

    private static final Class<UserSubscribeEntity> ENTITY_CLASS = UserSubscribeEntity.class;

    private final UserSubscribeRepository userSubscribeRepository;

    @Autowired
    private KarmaExchangeService karmaExchangeService;

    @Autowired
    private UserDeviceService userDeviceService;

    @Autowired
    private FirebaseService firebaseService;

    @Autowired
    public UserSubscribeServiceImpl(UserSubscribeRepository userSubscribeRepository,
                                    DefaultConverter defaultConverter) {
        super(userSubscribeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.userSubscribeRepository = userSubscribeRepository;
    }

    @Override
    public List<UserSubscribeDto> subscribe(List<UserSubscribeDto> subscribes, UUID userId, String registrationToken) {
        List<UserSubscribeDto> result = null;
        List<UserSubscribeDto> validSubscribes = validateDestinations(subscribes, userId);
        if (CollectionUtils.isNotEmpty(validSubscribes)) {
            result = super.create(validSubscribes);
            if (CollectionUtils.isNotEmpty(result)) {
                result.forEach(i -> firebaseService.subscribeToTopic(registrationToken, i.getSubscribeDestination().toString(), i.getObjectId()));
            }
        }
        return result;
    }

    @Override
    public List<UserSubscribeDto> getSubscribes(SubscribeDestination subscribeDestination, UUID objectId) {
        List<UserSubscribeDto> result = null;
        if (ObjectUtils.allNotNull(subscribeDestination, objectId)) {
            List<UserSubscribeEntity> entities = userSubscribeRepository.findAllBySubscribeDestinationAndObjectId(subscribeDestination, objectId);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<UserSubscribeDto> getByUserDeviceId(UUID userDeviceId) {
        List<UserSubscribeDto> result = null;
        if (userDeviceId != null) {
            List<UserSubscribeEntity> entities = userSubscribeRepository.findAllByUserDeviceId(userDeviceId);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<UserSubscribeDto> getAllByUserDeviceIdAndSubscribeDestination(List<UUID> userDeviceIds, SubscribeDestination subscribeDestination) {
        List<UserSubscribeDto> result = null;
        if (CollectionUtils.isNotEmpty(userDeviceIds) && (subscribeDestination != null)) {
            List<UserSubscribeEntity> entities = userSubscribeRepository.findAllByUserDeviceIdInAndSubscribeDestinationAndNotificationEnableTrue(userDeviceIds, subscribeDestination);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public List<UserSubscribeDto> getByRegistrationToken(String registrationToken) {
        List<UserSubscribeDto> result = null;
        UserDeviceDto userDevice = userDeviceService.getByRegistrationToken(registrationToken);
        if (userDevice != null) {
            result = getByUserDeviceId(userDevice.getId());
        }
        return result;
    }

    @Override
    @Transactional
    public void unsubscribeAll(UUID deviceId, String registrationToken) {
        if (deviceId != null) {
            List<UserSubscribeEntity> subscribes = userSubscribeRepository.findAllByUserDeviceId(deviceId);
            if (CollectionUtils.isNotEmpty(subscribes)) {
                subscribes.forEach(subscribe -> firebaseService.unsubscribeFromTopic(registrationToken, subscribe.getSubscribeDestination().toString(), subscribe.getObjectId()));
                userSubscribeRepository.deleteAll(subscribes);
            }
        }
    }

    @Override
    @Transactional
    public void unsubscribe(UUID deviceId, String registrationToken, SubscribeDestination subscribeDestination, UUID objectId) {
        if (deviceId != null) {
            UserSubscribeEntity subscribe = userSubscribeRepository.findByUserDeviceIdAndSubscribeDestinationAndObjectId(deviceId, subscribeDestination, objectId);
            if (subscribe != null) {
                firebaseService.unsubscribeFromTopic(registrationToken, subscribe.getSubscribeDestination().toString(), subscribe.getObjectId());
                userSubscribeRepository.delete(subscribe);
            }
        }
    }

    @Override
    @Transactional
    public List<UserSubscribeDto> addSubscribes(UserDeviceRegistrationDto userDeviceRegistration,
                                                Boolean overrideExistedDestination) {
        List<UserSubscribeDto> result = null;
        if ((userDeviceRegistration != null) && CollectionUtils.isNotEmpty(userDeviceRegistration.getSubscribes())) {
            UserDeviceDto userDevice = userDeviceService.getByRegistrationToken(userDeviceRegistration.getFirebaseRegistrationToken());
            if (userDevice == null) {
                throw new ClientException(REGISTRATION_TOKEN_NOT_EXIST);
            }
            UUID userDeviceId = userDevice.getId();
            List<UserSubscribeDto> subscribes = userDeviceRegistration.getSubscribes();
            List<UserSubscribeEntity> entities = userSubscribeRepository.findAllByUserDeviceId(userDeviceId);

            if (CollectionUtils.isNotEmpty(entities)) {
                subscribes = subscribes.stream()
                        .filter(i -> entities.stream()
                                .noneMatch(e ->
                                        e.getSubscribeDestination().equals(i.getSubscribeDestination()) &&
                                                (((e.getObjectId() == null) && (i.getObjectId() == null)) || (e.getObjectId().equals(i.getObjectId())))))
                        .peek(i -> i.setUserDeviceId(userDeviceId))
                        .collect(Collectors.toList());
            }
            subscribes = validateDestinations(subscribes, userDevice.getUserId());
            if (CollectionUtils.isNotEmpty(subscribes)) {
                if (overrideExistedDestination) {
                    List<SubscribeDestination> destinations = subscribes.stream()
                            .map(UserSubscribeDto::getSubscribeDestination)
                            .collect(Collectors.toList());
                    List<UserSubscribeEntity> toDelete = userSubscribeRepository.findAllByUserDeviceIdAndSubscribeDestinationIn(userDeviceId, destinations);
                    if (CollectionUtils.isNotEmpty(toDelete)) {
                        for (UserSubscribeEntity delete : toDelete) {
                            firebaseService.unsubscribeFromTopic(userDevice.getFirebaseRegistrationToken(), delete.getSubscribeDestination().toString(), delete.getObjectId());
                        }
                        userSubscribeRepository.deleteAll(toDelete);
                    }
                }
                result = super.create(subscribes);
                if (CollectionUtils.isNotEmpty(result)) {
                    result.forEach(i -> firebaseService.subscribeToTopic(userDevice.getFirebaseRegistrationToken(), i.getSubscribeDestination().toString(), i.getObjectId()));
                }
            }
        }
        return result;
    }

    private List<UserSubscribeDto> validateDestinations(List<UserSubscribeDto> subscribes, UUID userId) {
        List<UserSubscribeDto> validSubscribes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subscribes) && (userId != null)) {
            for (UserSubscribeDto subscribe : subscribes) {
                switch (subscribe.getSubscribeDestination()) {
                    case KARMA_USER_RECORD:
                    case KARMA_CHAT_USER:
                    case KARMA_USER_REMIND_RECORD: {
                        subscribe.setObjectId(userId);
                        validSubscribes.add(subscribe);
                        break;
                    }
                    case KARMA_BUSINESS_RECORD: {
                        if (subscribe.getObjectId() != null) {
                            List<BaseBusinessDto> business = karmaExchangeService.getBusinessForCurrentUser();
                            if (CollectionUtils.isNotEmpty(business) && business.stream().anyMatch(i -> i.getId().equals(subscribe.getObjectId()))) {
                                validSubscribes.add(subscribe);
                            }
                        }
                        break;
                    }
                    case KARMA_BUSINESS_NOTIFICATION:
                    case KARMA_USER_NEW_BUSINESS: {
                        subscribe.setObjectId(null);
                        validSubscribes.add(subscribe);
                        break;
                    }
                    case KARMA_CHAT_BUSINESS: {
                        if (subscribe.getObjectId() != null) {
                            if (karmaExchangeService.existChatSupport(subscribe.getObjectId(), userId)) {
                                validSubscribes.add(subscribe);
                            }
                        }
                        break;
                    }
                }
            }
        }
        return validSubscribes;
    }
}