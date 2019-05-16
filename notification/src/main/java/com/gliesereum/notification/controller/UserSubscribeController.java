package com.gliesereum.notification.controller;

import com.gliesereum.notification.service.subscribe.UserSubscribeService;
import com.gliesereum.share.common.model.dto.notification.subscribe.UserSubscribeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/by-registration-token")
    public List<UserSubscribeDto> getByRegistrationToken(@RequestParam("registrationToken") String registrationToken) {
        return userSubscribeService.getByRegistrationToken(registrationToken);
    }

    @GetMapping("/by-user-device")
    public List<UserSubscribeDto> getByUserDeviceId(@RequestParam("userDeviceId") UUID userDeviceId) {
        return userSubscribeService.getByUserDeviceId(userDeviceId);
    }
}
