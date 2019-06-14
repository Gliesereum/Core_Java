package com.gliesereum.share.common.exchange.service.account;

import com.gliesereum.share.common.model.dto.account.user.DetailedUserDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface UserExchangeService {

    boolean userIsExist(UUID userId);

//    boolean userKYCPassed(UUID userId);

    List<UserDto> findByIds(Collection<UUID> ids);

    List<DetailedUserDto> findDetailedByIds(Collection<UUID> ids);

    Map<UUID, UserDto> findUserMapByIds(Collection<UUID> ids);

    UserDto getByPhone(String phone);
}
