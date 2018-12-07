package com.gliesereum.proxy.service.keeper;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-07
 */
public interface EndpointKeeperService {

    void checkAccess(String currentJwt, String uri, String method);
}
