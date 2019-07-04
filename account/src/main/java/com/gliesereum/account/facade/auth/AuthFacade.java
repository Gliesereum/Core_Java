package com.gliesereum.account.facade.auth;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface AuthFacade {

    void tokenInfoUpdateEvent(List<UUID> userIds);
}
