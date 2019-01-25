package com.gliesereum.proxy.service.exchange.auth;

import com.gliesereum.share.common.model.dto.account.auth.AuthDto;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface AuthService {

    AuthDto checkAccessToken(String accessToken);
}
