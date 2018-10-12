package com.gliesereum.account.service.auth;

import com.gliesereum.share.common.model.dto.account.auth.AuthDto;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */
public interface AuthService {

    AuthDto signin();

    AuthDto signup();

    AuthDto check(String accessToken);
}
