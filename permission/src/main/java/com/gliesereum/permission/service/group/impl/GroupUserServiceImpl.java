package com.gliesereum.permission.service.group.impl;

import com.gliesereum.permission.model.entity.group.GroupUserEntity;
import com.gliesereum.permission.model.repository.jpa.group.GroupUserRepository;
import com.gliesereum.permission.service.group.GroupService;
import com.gliesereum.permission.service.group.GroupUserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.group.GroupUserDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.PermissionExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_FOUND;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class GroupUserServiceImpl extends DefaultServiceImpl<GroupUserDto, GroupUserEntity> implements GroupUserService {

    private static final Class<GroupUserDto> DTO_CLASS = GroupUserDto.class;
    private static final Class<GroupUserEntity> ENTITY_CLASS = GroupUserEntity.class;

    private GroupUserRepository groupUserRepository;

    @Autowired
    private GroupService groupService;

    @Autowired
    private UserExchangeService userExchangeService;

    @Autowired
    public GroupUserServiceImpl(GroupUserRepository groupUserRepository, DefaultConverter defaultConverter) {
        super(groupUserRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.groupUserRepository = groupUserRepository;
    }

    @Override
    public GroupUserDto addToGroup(GroupUserDto groupUser) {
        UUID userId = groupUser.getUserId();
        UUID groupId = groupUser.getGroupId();
        if (userId == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        if (!userExchangeService.userIsExist(userId)) {
            throw new ClientException(USER_NOT_FOUND);
        }
        if (!groupService.isExist(groupId)) {
            throw new ClientException(GROUP_NOT_FOUND);
        }
        if (groupUserRepository.existsByGroupIdAndUserId(groupId, userId)) {
            throw new ClientException(USER_EXIST_IN_GROUP);
        }
        return super.create(groupUser);
    }

    @Override
    public List<GroupUserDto> addToGroupByPurpose(GroupPurpose groupPurpose, UUID userId) {
        List<GroupUserDto> result = null;
        if (ObjectUtils.allNotNull(groupPurpose, userId)) {
            if (!userExchangeService.userIsExist(userId)) {
                throw new ClientException(USER_NOT_FOUND);
            }
            List<GroupDto> groups = groupService.getByPurposes(Arrays.asList(groupPurpose));
            if (CollectionUtils.isEmpty(groups)) {
                throw new ClientException(GROUP_NOT_FOUND);
            }
            List<GroupUserDto> groupsUser = groups.stream()
                    .filter(i -> !groupUserRepository.existsByGroupIdAndUserId(i.getId(), userId))
                    .map(i -> new GroupUserDto(i.getId(), userId))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(groupsUser)) {
                result = super.create(groupsUser);
            }
        }
        return result;
    }

    @Override
    public void removeFromGroup(UUID groupId, UUID userId) {
        if (userId == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        if (!userExchangeService.userIsExist(userId)) {
            throw new ClientException(USER_NOT_FOUND);
        }
        GroupUserEntity groupUser = groupUserRepository.findByGroupIdAndUserId(groupId, userId);
        if (groupUser == null) {
            throw new ClientException(USER_NOT_EXIST_IN_GROUP);
        }
        groupUserRepository.delete(groupUser);
    }

    @Override
    public List<GroupDto> getGroupByUser(UserDto user, UUID applicationId) {
        List<GroupDto> result;
        if ((user != null) && (user.getId() != null)) {
            UUID userId = user.getId();
            if (userId == null) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            if (!userExchangeService.userIsExist(userId)) {
                throw new ClientException(USER_NOT_FOUND);
            }
            result = groupService.getDefaultGroup(user, applicationId);
            result = (result == null) ? new ArrayList<>() : result;
    
            List<GroupUserEntity> groupUser = groupUserRepository.findByUserId(userId);
            if (CollectionUtils.isNotEmpty(groupUser)) {
                List<GroupDto> byIds = groupService.getByIds(groupUser.stream().map(GroupUserEntity::getGroupId).collect(Collectors.toList()));
                if (CollectionUtils.isNotEmpty(byIds)) {
                    result.addAll(byIds);
                }
            }
            result = result.stream().distinct().collect(Collectors.toList());
        } else {
            result = groupService.getForAnonymous(applicationId);
        }
        return result;
    }

    @Override
    public List<GroupUserDto> getByGroupPurpose(GroupPurpose groupPurpose) {
        List<GroupUserDto> result = null;
        if (groupPurpose != null) {
            List<GroupDto> groups = groupService.getByPurposes(Arrays.asList(groupPurpose));
            if (CollectionUtils.isNotEmpty(groups)) {
                List<UUID> groupIds = groups.stream().map(GroupDto::getId).collect(Collectors.toList());
                List<GroupUserEntity> entities = groupUserRepository.findAllByGroupIdIn(groupIds);
                result = converter.convert(entities, dtoClass);
            }
        }
        return result;
    }
    
    @Override
    public List<GroupDto> getByPhone(String phone, UUID applicationId) {
        List<GroupDto> result;
        UserDto user = null;
        if (phone != null) {
            user = userExchangeService.getByPhone(phone);
        }
        result = getGroupByUser(user, applicationId);
        return result;
    }
    
    @Override
    public boolean groupExistInUser(GroupPurpose groupPurpose) {
        boolean result = false;
        if (groupPurpose != null) {
            List<GroupDto> groups;
            if (SecurityUtil.isAnonymous()) {
                groups = groupService.getForAnonymous(SecurityUtil.getApplicationId());
            } else {
                SecurityUtil.checkUserByBanStatus();
                groups = this.getGroupByUser(SecurityUtil.getUserModel(), SecurityUtil.getApplicationId());
            }
            if (CollectionUtils.isNotEmpty(groups)) {
                result = groups.stream().anyMatch(i -> i.getPurpose().equals(groupPurpose));
            }
        }
        return result;
    }
}
