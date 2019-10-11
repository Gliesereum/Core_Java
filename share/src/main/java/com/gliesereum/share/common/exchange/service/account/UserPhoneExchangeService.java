package com.gliesereum.share.common.exchange.service.account;

import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
public interface UserPhoneExchangeService {

    List<UserPhoneDto> findUserPhoneByUserIds(Collection<UUID> ids);
    
    void sendCode(String phone);
}
