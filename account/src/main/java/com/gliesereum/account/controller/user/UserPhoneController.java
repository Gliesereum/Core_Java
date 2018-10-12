package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.*;

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
    public UserPhoneDto create(@NotNull Map<String, String> params) { //params: {'phone': phone(String), 'code': code(String)}
        String phone = params.get("phone");
        String code = params.get("code");
        if (StringUtils.isEmpty(phone)) {
            throw new ClientException(PHONE_EMPTY);
        }
        if (StringUtils.isEmpty(code)) {
            throw new ClientException(PHONE_CODE_EMPTY);
        }
        return phoneService.create(phone,code);
    }

    @PutMapping
    public UserPhoneDto update(@NotNull Map<String, String> params) { //params: {'phone': phone(String), 'code': code(String)}
        String phone = params.get("phone");
        String code = params.get("code");
        if (StringUtils.isEmpty(phone)) {
            throw new ClientException(PHONE_EMPTY);
        }
        if (StringUtils.isEmpty(code)) {
            throw new ClientException(PHONE_CODE_EMPTY);
        }
        return phoneService.update(phone,code);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable("id") UUID id) {
        phoneService.delete(id);
        Map<String, String> result = new HashMap<>();
        result.put("deleted", "true");
        return result;
    }

    @GetMapping("/by/user/id/{id}")
    public UserPhoneDto getByUserId(@PathVariable("id") UUID id) {
        return phoneService.getByUserId(id);
    }

    @GetMapping("/code")
    public Map<String, String> sendCode(@NotNull Map<String, String> params) { //params: {'phone': phone(String)}
        String phone = params.get("phone");
        if (StringUtils.isEmpty(phone)) {
            throw new ClientException(PHONE_EMPTY);
        }
        phoneService.sendCode(phone);
        Map<String, String> result = new HashMap<>();
        result.put("sent", "true");
        return result;
    }
}
