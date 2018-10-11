package com.gliesereum.account.service.auth.impl;

import com.gliesereum.account.service.auth.AuthService;
import com.gliesereum.share.common.model.dto.auth.AuthDto;
import org.springframework.stereotype.Service;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Override
    public AuthDto signin() {
        return null;
    }

    @Override
    public AuthDto signup() {
        return null;
    }

    @Override
    public AuthDto auth(String accessToken) {
        return null;
    }
}
