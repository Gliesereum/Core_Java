package com.gliesereum.account.service.auth.impl;

import com.gliesereum.account.model.domain.TokenStoreDomain;
import com.gliesereum.account.service.auth.AuthService;
import com.gliesereum.account.service.token.TokenService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.account.auth.TokenInfoDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private DefaultConverter defaultConverter;

    @Override
    public AuthDto signin() {
        return null;
    }

    @Override
    public AuthDto signup() {
        return null;
    }

    @Override
    public AuthDto check(String accessToken) {
        TokenStoreDomain token = tokenService.getAndVerify(accessToken);
        UserDto user = userService.getById(UUID.fromString(token.getUserId()));
        return createModel(token, user);
    }

    private AuthDto createModel(TokenStoreDomain tokenStore, UserDto user) {
        AuthDto authDto = new AuthDto();
        authDto.setTokenInfo(defaultConverter.convert(tokenStore, TokenInfoDto.class));
        authDto.setUser(user);
        return authDto;
    }
}
