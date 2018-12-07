package com.gliesereum.account.controller.auth;

import com.gliesereum.account.model.domain.TokenStoreDomain;
import com.gliesereum.account.service.auth.AuthService;
import com.gliesereum.account.service.token.TokenService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private TokenService tokenService;

    @PostMapping("/signup")
    public AuthDto signUp(@RequestBody Map<String, String> params) { //params: {'value': value(String), 'code': code(String),'type': type(PHONE,EMAIL)}
        return authService.signUp(params);
    }

    @PostMapping("/signin")
    public AuthDto signIn(@RequestBody Map<String, String> params) { //params: {'value': value(String), 'code': code(String),'type': type(PHONE,EMAIL)}
        return authService.signIn(params);
    }

    @GetMapping("/check")
    public AuthDto check(@RequestParam("accessToken") String accessToken) {
        return authService.check(accessToken);
    }

    @PostMapping("/refresh")
    public TokenStoreDomain refresh(@RequestParam("accessToken") String accessToken,
                                    @RequestParam("refreshToken") String refreshToken) {
        return tokenService.refresh(accessToken, refreshToken);
    }

    @PostMapping("/revoke")
    public MapResponse revoke(@RequestParam("accessToken") String accessToken) {
        tokenService.revoke(accessToken);
        return new MapResponse("true");
    }
}
