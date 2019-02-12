package com.gliesereum.share.common.exchange.service.account;

import com.gliesereum.share.common.model.dto.account.user.UserDto;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface UserExchangeService {

    boolean userIsExist(UUID userId);

    boolean userKYCPassed(UUID userId);

    List<UserDto> findByIds(List<UUID> ids);
}
