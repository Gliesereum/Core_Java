package com.gliesereum.notification.service.device;

import com.gliesereum.notification.model.entity.device.UserDeviceEntity;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceDto;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceRegistrationDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

public interface UserDeviceService extends DefaultService<UserDeviceDto, UserDeviceEntity> {

    UserDeviceDto registerDevice(UserDeviceRegistrationDto userDeviceRegistration);

    List<UserDeviceDto> getByUserId(UUID userId);

    UserDeviceDto getByRegistrationToken(String registrationToken);

    void removeDevice(String registrationToken);
}