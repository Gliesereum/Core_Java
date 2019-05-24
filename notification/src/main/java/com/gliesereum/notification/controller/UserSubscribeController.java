package com.gliesereum.notification.controller;

import com.gliesereum.notification.service.subscribe.UserSubscribeService;
import com.gliesereum.share.common.model.dto.notification.device.UserDeviceRegistrationDto;
import com.gliesereum.share.common.model.dto.notification.subscribe.UserSubscribeDto;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
@RequestMapping("/user-subscribe")
public class UserSubscribeController {

    @Autowired
    private UserSubscribeService userSubscribeService;

    @PostMapping("/list")
    public List<UserSubscribeDto> addSubscribes(@Valid @RequestBody UserDeviceRegistrationDto userDeviceRegistration,
                                                @RequestParam("overrideExistedDestination") Boolean overrideExistedDestination) {
        SecurityUtil.checkUserByBanStatus();
        userDeviceRegistration.setUserId(SecurityUtil.getUserId());
        return userSubscribeService.addSubscribes(userDeviceRegistration, overrideExistedDestination);
    }

    @GetMapping("/by-registration-token")
    public List<UserSubscribeDto> getByRegistrationToken(@RequestParam("registrationToken") String registrationToken) {
        return userSubscribeService.getByRegistrationToken(registrationToken);
    }

    @GetMapping("/by-user-device")
    public List<UserSubscribeDto> getByUserDeviceId(@RequestParam("userDeviceId") UUID userDeviceId) {
        return userSubscribeService.getByUserDeviceId(userDeviceId);
    }
}
