package com.gliesereum.account.facade.auth;

import com.gliesereum.share.common.model.dto.account.user.UserDto;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface UserUpdateFacade {

    void tokenInfoUpdateEvent(List<UUID> userIds);

    void updateClientInfo(UserDto user);
}
