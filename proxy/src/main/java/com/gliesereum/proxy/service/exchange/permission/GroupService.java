package com.gliesereum.proxy.service.exchange.permission;

import com.gliesereum.share.common.model.dto.permission.permission.PermissionMapValue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface GroupService {

    Map<String, PermissionMapValue> getPermissionMap(List<UUID> groupId);
}
