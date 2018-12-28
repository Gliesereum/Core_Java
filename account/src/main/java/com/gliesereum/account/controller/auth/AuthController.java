package com.gliesereum.account.controller.auth;

import com.gliesereum.account.service.auth.AuthService;
import com.gliesereum.account.service.token.TokenService;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.account.auth.SignInDto;
import com.gliesereum.share.common.model.dto.account.auth.TokenInfoDto;
import com.gliesereum.share.common.model.response.MapResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping("/signin")
    public AuthDto signIn(@Valid @RequestBody SignInDto signInDto) { //params: {'value': value(String), 'code': code(String),'type': type(PHONE,EMAIL)}
        return authService.signIn(signInDto);
    }

    //TODO: merge signin with signup
    /*@PostMapping("/signup")
    public AuthDto signUp(@RequestBody Map<String, String> params) { //params: {'value': value(String), 'code': code(String),'type': type(PHONE,EMAIL)}
        return authService.signUp(params);
    }

    @PostMapping("/signin")
    public AuthDto signIn(@RequestBody Map<String, String> params) { //params: {'value': value(String), 'code': code(String),'type': type(PHONE,EMAIL)}
        return authService.signIn(params);
    }*/

    @GetMapping("/check")
    public AuthDto check(@RequestParam("accessToken") String accessToken) {
        return authService.check(accessToken);
    }

    @PostMapping("/refresh")
    public TokenInfoDto refresh(@RequestParam("accessToken") String accessToken,
                                @RequestParam("refreshToken") String refreshToken) {
        return tokenService.refresh(accessToken, refreshToken);
    }

    @PostMapping("/revoke")
    public MapResponse revoke(@RequestParam("accessToken") String accessToken) {
        tokenService.revoke(accessToken);
        return new MapResponse("true");
    }
}
