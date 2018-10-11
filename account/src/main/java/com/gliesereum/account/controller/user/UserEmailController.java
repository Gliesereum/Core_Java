package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.share.common.model.dto.account.user.UserEmailDto;
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
@RequestMapping("/email")
public class UserEmailController {

    @Autowired
    private UserEmailService emailService;

    @GetMapping("/{id}")
    public UserEmailDto getById(@PathVariable("id") UUID id) {
        return emailService.getById(id);
    }

    @GetMapping
    public List<UserEmailDto> getAll() {
        return emailService.getAll();
    }

    @PostMapping
    public UserEmailDto create(@RequestBody UserEmailDto userEmail) {
        return emailService.create(userEmail);
    }

    @PutMapping
    public UserEmailDto update(@RequestBody UserEmailDto userEmail) {
        return emailService.update(userEmail);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable("id") UUID id) {
        emailService.delete(id);
        Map<String, String> result = new HashMap<>();
        result.put("deleted", "true");
        return result;
    }
}
