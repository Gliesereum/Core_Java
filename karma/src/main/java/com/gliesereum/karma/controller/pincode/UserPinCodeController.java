package com.gliesereum.karma.controller.pincode;

import com.gliesereum.karma.service.pincode.UserPinCodeService;
import com.gliesereum.share.common.model.dto.karma.pincode.UserPinCodeDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@RequestMapping("/user-pin-code")
@RestController
public class UserPinCodeController {

    @Autowired
    private UserPinCodeService userPinCodeService;

    @GetMapping
    public UserPinCodeDto getByUser() {
        SecurityUtil.checkUserByBanStatus();
        return userPinCodeService.getByUserId(SecurityUtil.getUserId());
    }

    @PostMapping
    public UserPinCodeDto save(@Valid @RequestBody UserPinCodeDto userPinCode) {
        SecurityUtil.checkUserByBanStatus();
        userPinCode.setUserId(SecurityUtil.getUserId());
        return userPinCodeService.save(userPinCode);
    }

    @DeleteMapping
    public MapResponse deleteByUser() {
        SecurityUtil.checkUserByBanStatus();
        userPinCodeService.deleteByUserId(SecurityUtil.getUserId());
        return MapResponse.resultTrue();
    }

    @PostMapping("/remind-me")
    public MapResponse remindMe() {
        SecurityUtil.checkUserByBanStatus();
        userPinCodeService.remindMe();
        return new MapResponse("true");
    }
}
