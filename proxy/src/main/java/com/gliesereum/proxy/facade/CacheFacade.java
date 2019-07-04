package com.gliesereum.proxy.facade;

import com.gliesereum.share.common.model.dto.account.auth.AuthDto;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface CacheFacade {

    void updateAuthCache(List<AuthDto> authInfo);
}
