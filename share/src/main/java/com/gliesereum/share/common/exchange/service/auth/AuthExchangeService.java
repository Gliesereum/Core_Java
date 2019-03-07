package com.gliesereum.share.common.exchange.service.auth;

import com.gliesereum.share.common.model.dto.account.auth.AuthDto;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface AuthExchangeService {

    AuthDto checkAccessToken(String accessToken);
}
