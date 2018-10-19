package com.gliesereum.proxy.service.auth;

import com.gliesereum.share.common.model.dto.account.auth.AuthDto;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 16/10/2018
 */
public interface AuthService {

    AuthDto checkAccessToken(String accessToken);
}
