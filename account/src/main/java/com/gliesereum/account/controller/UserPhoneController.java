package com.gliesereum.account.controller;

import com.gliesereum.account.service.UserPhoneService;
import com.gliesereum.share.common.model.dto.account.UserPhoneDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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

    @GetMapping("/{id}")
    public UserPhoneDto getById(@PathVariable("id") UUID id) {
        return phoneService.getById(id);
    }

    @GetMapping
    public List<UserPhoneDto> getAll() {
        return phoneService.getAll();
    }

    @PostMapping
    public UserPhoneDto create(@RequestBody UserPhoneDto userPhone) {
        return phoneService.create(userPhone);
    }

    @PutMapping
    public UserPhoneDto update(@RequestBody UserPhoneDto userPhone) {
        return phoneService.update(userPhone);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable("id") UUID id) {
        phoneService.delete(id);
        Map<String, String> result = new HashMap<>();
        result.put("deleted", "true");
        return result;
    }
}
