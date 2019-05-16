package com.gliesereum.notification.controller;

import com.gliesereum.notification.service.device.UserDeviceService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exception.messages.CommonExceptionMessage;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceDto;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceRegistrationDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
@RequestMapping("/user-device")
public class UserDeviceController {

    @Autowired
    private UserDeviceService userDeviceService;

    @PostMapping
    public UserDeviceDto addUserDevice(@Valid @RequestBody UserDeviceRegistrationDto userDeviceRegistration) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(CommonExceptionMessage.USER_IS_ANONYMOUS);
        }
        userDeviceRegistration.setUserId(SecurityUtil.getUserId());
        return userDeviceService.registerDevice(userDeviceRegistration);
    }

    @DeleteMapping
    public MapResponse deleteDevice(@RequestParam("registrationToken") String registrationToken) {
        userDeviceService.removeDevice(registrationToken);
        return MapResponse.resultTrue();
    }
}
