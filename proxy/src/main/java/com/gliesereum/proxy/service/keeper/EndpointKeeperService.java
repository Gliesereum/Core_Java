package com.gliesereum.proxy.service.keeper;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface EndpointKeeperService {

    void checkAccess(String currentJwt, String uri, String method);
}
