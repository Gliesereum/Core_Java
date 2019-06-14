package com.gliesereum.account.controller.auth;

import com.gliesereum.account.service.auth.AuthService;
import com.gliesereum.account.service.token.TokenService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.account.auth.SignInDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author vitalij
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/signin")
    public AuthDto signIn(@Valid @RequestBody SignInDto signInDto) {
        return authService.signIn(signInDto);
    }

    //TODO: merge signin with signup
    /*@PostMapping("/signup")
    public AuthDto signUp(@RequestBody Map<String, String> params) {
        return authService.signUp(params);
    }

    @PostMapping("/signin")
    public AuthDto signIn(@RequestBody Map<String, String> params) {
        return authService.signIn(params);
    }*/

    @GetMapping("/check")
    public AuthDto check(@RequestParam("accessToken") String accessToken) {
        return authService.check(accessToken);
    }

    @PostMapping("/refresh")
    public AuthDto refresh(@RequestParam("refreshToken") String refreshToken) {
        return authService.refresh(refreshToken);
    }

    @PostMapping("/revoke")
    public MapResponse revoke(@RequestParam("accessToken") String accessToken) {
        tokenService.revoke(accessToken);
        return new MapResponse("true");
    }
}
