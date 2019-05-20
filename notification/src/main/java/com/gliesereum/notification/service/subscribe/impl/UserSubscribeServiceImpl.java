package com.gliesereum.notification.service.subscribe.impl;

import com.gliesereum.notification.model.entity.subscribe.UserSubscribeEntity;
import com.gliesereum.notification.model.repository.jpa.subscribe.UserSubscribeRepository;
import com.gliesereum.notification.service.device.UserDeviceService;
import com.gliesereum.notification.service.subscribe.UserSubscribeService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exchange.service.karma.KarmaExchangeService;
import com.gliesereum.share.common.model.dto.karma.business.BaseBusinessDto;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceDto;
import com.gliesereum.share.common.model.dto.notification.enumerated.SubscribeDestination;
import com.gliesereum.share.common.model.dto.notification.subscribe.UserSubscribeDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    public UserSubscribeServiceImpl(UserSubscribeRepository userSubscribeRepository,
                                    DefaultConverter defaultConverter) {
        super(userSubscribeRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.userSubscribeRepository = userSubscribeRepository;
    }

    @Override
    public List<UserSubscribeDto> subscribe(List<UserSubscribeDto> subscribes, UUID userId) {
        List<UserSubscribeDto> result = null;
        List<UserSubscribeDto> validSubscribes = validateDestinations(subscribes, userId);
        if (CollectionUtils.isNotEmpty(validSubscribes)) {
            result = super.create(validSubscribes);
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
    public List<UserSubscribeDto> getByRegistrationToken(String registrationToken) {
        List<UserSubscribeDto> result = null;
        UserDeviceDto userDevice = userDeviceService.getByRegistrationToken(registrationToken);
        if (userDevice != null) {
            result = getByUserDeviceId(userDevice.getId());
        }
        return result;
    }

    @Override
    public void deleteByDeviceId(UUID deviceId) {
        if (deviceId != null) {
            userSubscribeRepository.deleteAllByUserDeviceId(deviceId);
        }
    }

    private List<UserSubscribeDto> validateDestinations(List<UserSubscribeDto> subscribes, UUID userId) {
        List<UserSubscribeDto> validSubscribes = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subscribes) && (userId != null)) {
            for (UserSubscribeDto subscribe : subscribes) {
                switch (subscribe.getSubscribeDestination()) {
                    case KARMA_USER_RECORD:
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
                    case KARMA_USER_NEW_BUSINESS: {
                        validSubscribes.add(subscribe);
                        break;
                    }
                }
            }
        }
        return validSubscribes;
    }
}