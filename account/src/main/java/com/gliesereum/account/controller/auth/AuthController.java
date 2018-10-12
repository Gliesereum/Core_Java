package com.gliesereum.account.controller.auth;

import com.gliesereum.account.service.auth.AuthService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author vitalij
 * @since 12/10/2018
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup")
    public AuthDto signup(@NotNull Map<String, String> params) { //params: {'value': value(String), 'code': code(String),'type': type(PHONE,EMAIL)}
        return authService.signup(params);
    }

    @PostMapping("/signin")
    public AuthDto signin() {
        return authService.signin();
    }

}
