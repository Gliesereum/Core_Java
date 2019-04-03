package com.gliesereum.proxy.service.exchange.permission;

import com.gliesereum.share.common.model.dto.permission.group.GroupDto;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface GroupUserService {

    List<GroupDto> getUserGroups(UUID id, String jwt);
}
