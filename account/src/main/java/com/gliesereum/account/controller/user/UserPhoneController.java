package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@RestController
@RequestMapping("/phone")
public class UserPhoneController {

    @Autowired
    private UserPhoneService phoneService;

   /* @PutMapping("/by/user/id/{id}")
    public UserPhoneDto update(@PathVariable("id") UUID id,
                               @PathVariable("phone") String phone) {
        return phoneService.update(phone);
    }*/

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
    public Map<String, String> sendCode(@PathVariable("phone") String phone) {
        phoneService.sendCode(phone);
        Map<String, String> result = new HashMap<>();
        result.put("sent", "true");
        return result;
    }
}
