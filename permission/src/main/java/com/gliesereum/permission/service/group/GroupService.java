package com.gliesereum.permission.service.group;

import com.gliesereum.permission.model.entity.group.GroupEntity;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.permission.PermissionMapValue;
import com.gliesereum.share.common.service.DefaultService;

import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface GroupService extends DefaultService<GroupDto, GroupEntity> {

    GroupDto getDefaultGroup(UserDto user);

    GroupDto getForAnonymous();

    GroupDto getByPurpose(GroupPurpose purpose);

    Map<String, PermissionMapValue> getPermissionMap(UUID groupId);
}
