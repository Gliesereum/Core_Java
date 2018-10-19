package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") UUID id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll();
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto user) {
        return userService.create(user);
    }

    @PutMapping
    public UserDto update(@RequestBody UserDto user) {
        return userService.update(user);
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
    public Map<String, String> banById(@PathVariable("id") UUID id) {
        return userService.banById(id);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable("id") UUID id) {
        userService.delete(id);
        Map<String, String> result = new HashMap<>();
        result.put("deleted", "true");
        return result;
    }
}
