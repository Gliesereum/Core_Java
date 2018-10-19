package com.gliesereum.account.controller.user;

import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.user.UserEmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.EmailExceptionMessage.*;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@RestController
@RequestMapping("/email")
public class UserEmailController {

    @Autowired
    private UserEmailService emailService;

    @PostMapping
    public UserEmailDto create(@NotNull Map<String, String> params) { //params: {'email': email(String), 'code': code(String)}
        String email = params.get("email");
        String code = params.get("code");
        if (StringUtils.isEmpty(email)) {
            throw new ClientException(EMAIL_EMPTY);
        }
        if (StringUtils.isEmpty(code)) {
            throw new ClientException(EMAIL_CODE_EMPTY);
        }
        return emailService.create(email, code);
    }

    @PutMapping
    public UserEmailDto update(@NotNull Map<String, String> params) { //params: {'email': email(String), 'code': code(String)}
        String email = params.get("email");
        String code = params.get("code");
        if (StringUtils.isEmpty(email)) {
            throw new ClientException(EMAIL_EMPTY);
        }
        if (StringUtils.isEmpty(code)) {
            throw new ClientException(EMAIL_CODE_EMPTY);
        }
        return emailService.update(email, code);
    }

    @DeleteMapping("/{id}")
    public Map<String, String> delete(@PathVariable("id") UUID id) {
        emailService.delete(id);
        Map<String, String> result = new HashMap<>();
        result.put("deleted", "true");
        return result;
    }

    @GetMapping("/by/user/id/{id}")
    public UserEmailDto getByUserId(@PathVariable("id") UUID id) {
        return emailService.getByUserId(id);
    }

    @GetMapping("/code")
    public Map<String, String> sendCode(@RequestParam(value = "email") String email) {
        emailService.sendCode(email);
        Map<String, String> result = new HashMap<>();
        result.put("sent", "true");
        return result;
    }
}
