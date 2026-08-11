package com.gliesereum.proxy.service.keeper;

import com.gliesereum.share.common.security.model.UserAuthentication;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface EndpointKeeperService {

    /**
     * @param authentication the caller, passed explicitly because the reactive
     *                       gateway has no thread-bound SecurityContext to read
     *                       it back from.
     */
    void checkAccess(UserAuthentication authentication, String currentJwt, String uri, String method);
}
