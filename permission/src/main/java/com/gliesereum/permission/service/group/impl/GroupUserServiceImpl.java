package com.gliesereum.permission.service.group.impl;

import com.gliesereum.permission.model.entity.group.GroupUserEntity;
import com.gliesereum.permission.model.repository.jpa.group.GroupRepository;
import com.gliesereum.permission.model.repository.jpa.group.GroupUserRepository;
import com.gliesereum.permission.service.group.GroupService;
import com.gliesereum.permission.service.group.GroupUserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.group.GroupUserDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.GroupExceptionMessage.*;
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
        if (userId == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        if (!userExchangeService.userIsExist(userId)) {
            throw new ClientException(USER_NOT_FOUND);
        }
        if (groupUserRepository.findByUserId(userId) != null) {
            throw new ClientException(USER_EXIST_IN_GROUP);
        }
        if (!groupService.isExist(groupUser.getGroupId())) {
            throw new ClientException(GROUP_NOT_FOUND);
        }
        GroupUserDto result = super.create(groupUser);
        return result;
    }

    @Override
    public void removeFromGroup(UUID userId) {
        if (userId == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        if (!userExchangeService.userIsExist(userId)) {
            throw new ClientException(USER_NOT_FOUND);
        }
        GroupUserEntity groupUser = groupUserRepository.findByUserId(userId);
        if (groupUser == null) {
            throw new ClientException(USER_NOT_EXIST_IN_GROUP);
        }
        groupUserRepository.delete(groupUser);
    }

    @Override
    public GroupDto getGroupByUser(UserDto user) {
        GroupDto result;
        UUID userId = user.getId();
        if (userId == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        if (!userExchangeService.userIsExist(userId)) {
            throw new ClientException(USER_NOT_FOUND);
        }
        GroupUserEntity groupUser = groupUserRepository.findByUserId(userId);
        if (groupUser == null) {
            result = groupService.getDefaultGroup(user);
        } else {
            result = groupService.getById(groupUser.getGroupId());
        }
        return result;
    }
}
