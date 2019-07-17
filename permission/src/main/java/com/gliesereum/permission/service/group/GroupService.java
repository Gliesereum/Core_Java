package com.gliesereum.permission.service.group;

import com.gliesereum.permission.model.entity.group.GroupEntity;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.permission.PermissionMapValue;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface GroupService extends DefaultService<GroupDto, GroupEntity> {

    List<GroupDto> getDefaultGroup(UserDto user, UUID applicationId);

    List<GroupDto> getForAnonymous(UUID applicationId);

    List<GroupDto> getByPurposesAndApplicationId(List<GroupPurpose> purposes, UUID applicationId);

    List<GroupDto> getByPurposes(List<GroupPurpose> purposes);

    Map<String, PermissionMapValue> getPermissionMap(List<UUID> groupIds);
}
