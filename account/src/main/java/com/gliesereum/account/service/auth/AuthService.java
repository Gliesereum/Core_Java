package com.gliesereum.account.service.auth;

import com.gliesereum.share.common.model.dto.account.auth.AuthDto;

import java.util.Map;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */
public interface AuthService {

    AuthDto signIn(Map<String, String> params);

    AuthDto signUp(Map<String, String> params);

    AuthDto check(String accessToken);
}
