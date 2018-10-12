package com.gliesereum.account.controller.auth;

import com.gliesereum.account.service.auth.AuthService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public AuthDto signup() {
        return authService.signup();
    }

    @PostMapping("/signin")
    public AuthDto signin() {
        return authService.signin();
    }

    @GetMapping("/check")
    public AuthDto check(@RequestParam("accessToken") String accessToken) {
        return authService.check(accessToken);
    }

}
