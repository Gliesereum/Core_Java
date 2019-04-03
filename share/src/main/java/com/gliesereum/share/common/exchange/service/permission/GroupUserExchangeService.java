package com.gliesereum.share.common.exchange.service.permission;

import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.model.dto.permission.group.GroupUserDto;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface GroupUserExchangeService {

    List<GroupUserDto> addUserByGroupPurpose(UUID userId, GroupPurpose groupPurpose);
}
