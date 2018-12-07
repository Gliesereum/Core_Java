package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.PHONE_CODE_EMPTY;
import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.PHONE_EMPTY;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@RestController
@RequestMapping("/phone")
public class UserPhoneController {

    @Autowired
    private UserPhoneService phoneService;

    @PostMapping
    public UserPhoneDto create(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String code = params.get("code");
        if (StringUtils.isEmpty(phone)) {
            throw new ClientException(PHONE_EMPTY);
        }
        if (StringUtils.isEmpty(code)) {
            throw new ClientException(PHONE_CODE_EMPTY);
        }
        return phoneService.create(phone, code);
    }

    @PutMapping
    public UserPhoneDto update(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String code = params.get("code");
        if (StringUtils.isEmpty(phone)) {
            throw new ClientException(PHONE_EMPTY);
        }
        if (StringUtils.isEmpty(code)) {
            throw new ClientException(PHONE_CODE_EMPTY);
        }
        return phoneService.update(phone, code);
    }

    @DeleteMapping("/{id}")
    public MapResponse delete(@PathVariable("id") UUID id) {
        phoneService.delete(id);
        return new MapResponse("true");
    }

    @GetMapping("/by/user")
    public UserPhoneDto getByUserId() {
        return phoneService.getByUserId(SecurityUtil.getUserId());
    }

    @GetMapping("/code")
    public MapResponse sendCode(@RequestParam(value = "phone") String phone,
                                @RequestParam(value = "isNew") boolean isNew) {
        phoneService.sendCode(phone, isNew);
        return new MapResponse("sent", "true");
    }
}
