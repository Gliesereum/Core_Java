package com.gliesereum.proxy.service.exchange.permission;

import com.gliesereum.share.common.model.dto.permission.group.GroupDto;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-07
 */
public interface GroupUserService {

    GroupDto getUserGroup(String jwt);
}
