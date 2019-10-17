package com.gliesereum.permission.service.group;

import com.gliesereum.permission.model.entity.group.GroupUserEntity;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.group.GroupUserDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface GroupUserService extends DefaultService<GroupUserDto, GroupUserEntity> {

    GroupUserDto addToGroup(GroupUserDto groupUser);

    List<GroupUserDto> addToGroupByPurpose(GroupPurpose groupPurpose, UUID userId);

    void removeFromGroup(UUID groupId, UUID userId);

    List<GroupDto> getGroupByUser(UserDto user, UUID applicationId);

    List<GroupUserDto> getByGroupPurpose(GroupPurpose groupPurpose);
    
    List<GroupDto> getByPhone(String phone, UUID applicationId);

    boolean groupExistInUser(GroupPurpose groupPurpose);
}
