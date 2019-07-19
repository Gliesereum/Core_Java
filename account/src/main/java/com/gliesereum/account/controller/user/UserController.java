package com.gliesereum.account.controller.user;

import com.gliesereum.account.facade.user.UserFacade;
import com.gliesereum.account.service.referral.ReferralCodeService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.model.dto.account.referral.ReferralCodeDto;
import com.gliesereum.share.common.model.dto.account.user.DetailedUserDto;
import com.gliesereum.share.common.model.dto.account.user.PublicUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.response.MapResponse;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private ReferralCodeService referralCodeService;

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") UUID id) {
        return userService.getById(id);
    }

    @GetMapping("/by-ids")
    public List<UserDto> getByIds(@RequestParam("ids") List<UUID> ids) {
        return userService.getByIds(ids);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PostMapping("/public/create-or-get")
    public PublicUserDto createOrGetPublicUser(@RequestBody PublicUserDto user) {
        return userService.createOrGetPublicUser(user);
    }

    @PutMapping
    public UserDto update(@Valid @RequestBody UserDto user) {
        return userService.updateMe(user);
    }

    @GetMapping("/me")
    public UserDto getMe() {
        UserDto result = null;
        UUID userId = SecurityUtil.getUserId();
        if (userId != null) {
            result = userService.getById(userId);
        }
        return result;
    }

    @GetMapping("/ban/{id}")
    public MapResponse banById(@PathVariable("id") UUID id) {
        userService.banById(id);
        return new MapResponse("ban", "succeed");
    }

    @GetMapping("/un-ban/{id}")
    public MapResponse unBanById(@PathVariable("id") UUID id) {
        userService.unBanById(id);
        return new MapResponse("unBan", "succeed");
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        userService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/exist")
    public MapResponse userIsExist(@RequestParam("id") UUID id) {
        return new MapResponse(userService.isExist(id));
    }

    @GetMapping("/by-phone")
    public UserDto getByPhone(@RequestParam("phone") String phone) {
        return userService.getByPhone(phone);
    }

    @GetMapping("/detailed/by-ids")
    public List<DetailedUserDto> getDetailedByIds(@RequestParam("ids") List<UUID> ids) {
        return userFacade.getDetailedByIds(ids);
    }

    @GetMapping("/public/by-ids")
    public List<PublicUserDto> getPublicUsersByIds(@RequestParam("ids") List<UUID> ids) {
        return userFacade.getPublicUserByIds(ids);
    }

    @GetMapping("/referral-code/me")
    public ReferralCodeDto referralCodeForCurrentUser() {
        SecurityUtil.checkUserByBanStatus();
        return referralCodeService.getOrCreate(SecurityUtil.getUserId());
    }

}
