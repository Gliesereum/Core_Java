package com.gliesereum.permission.service.group.impl;

import com.gliesereum.permission.model.entity.endpoint.EndpointEntity;
import com.gliesereum.permission.model.entity.group.GroupEntity;
import com.gliesereum.permission.model.entity.module.ModuleEntity;
import com.gliesereum.permission.model.repository.jpa.group.GroupRepository;
import com.gliesereum.permission.service.group.GroupService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.module.ModuleDto;
import com.gliesereum.share.common.model.dto.permission.permission.PermissionMapValue;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.GroupExceptionMessage.GROUP_NOT_FOUND;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Service
public class GroupServiceImpl extends DefaultServiceImpl<GroupDto, GroupEntity> implements GroupService {

    private static final Class<GroupDto> DTO_CLASS = GroupDto.class;
    private static final Class<GroupEntity> ENTITY_CLASS = GroupEntity.class;

    private GroupRepository groupRepository;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, DefaultConverter defaultConverter) {
        super(groupRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupDto> getDefaultGroup(UserDto user) {
        List<GroupPurpose> groupPurposes = new ArrayList<>();
        if (user.getBanStatus().equals(BanStatus.BAN)) {
            groupPurposes.add(GroupPurpose.BANNED);
        } else {
            groupPurposes.add(GroupPurpose.ANONYMOUS);
            groupPurposes.add(GroupPurpose.AUTH);
            if (BooleanUtils.isTrue(user.getKycApproved())) {
                groupPurposes.add(GroupPurpose.KYC_PASSED);
            }
            if (CollectionUtils.isNotEmpty(user.getCorporationIds())) {
                groupPurposes.add(GroupPurpose.CORPORATION_USER);
            }
        }
        return getByPurposes(groupPurposes);
    }

    @Override
    public List<GroupDto> getForAnonymous() {
        return getByPurposes(Arrays.asList(GroupPurpose.ANONYMOUS));
    }

    @Override
    public List<GroupDto> getByPurposes(List<GroupPurpose> purposes) {
        List<GroupDto> result = null;
        if (CollectionUtils.isNotEmpty(purposes)) {
            List<GroupEntity> entities = groupRepository.findByPurposeIn(purposes);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public Map<String, PermissionMapValue> getPermissionMap(List<UUID> groupIds) {
        if (CollectionUtils.isEmpty(groupIds)) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        List<GroupEntity> groups = repository.findAllById(groupIds);
        if (CollectionUtils.isEmpty(groups)) {
            throw new ClientException(GROUP_NOT_FOUND);
        }
        List<GroupEntity> allGroups = groups.stream()
                .flatMap(i -> getAllParentGroups(i, new ArrayList<>()).stream())
                .collect(Collectors.toList());
        return convertToMap(allGroups);
    }

    public Map<String, PermissionMapValue> convertToMap(List<GroupEntity> groups) {
        Map<String, PermissionMapValue> result = null;
        if (CollectionUtils.isNotEmpty(groups)) {
            result = new HashMap<>();
            Set<EndpointEntity> endpointEntities = groups.stream()
                    .flatMap(i -> i.getEndpoints().stream())
                    .collect(Collectors.toSet());

            for (EndpointEntity endpoint : endpointEntities) {
                ModuleEntity module = endpoint.getModule();
                String moduleUrl = module.getUrl();
                if (result.containsKey(moduleUrl)) {
                    PermissionMapValue existedValue = result.get(moduleUrl);
                    existedValue.getEndpoints().add(converter.convert(endpoint, EndpointDto.class));
                } else {
                    PermissionMapValue newValue = new PermissionMapValue();
                    newValue.setModule(converter.convert(module, ModuleDto.class));
                    List<EndpointDto> list = new ArrayList<>();
                    list.add(converter.convert(endpoint, EndpointDto.class));
                    newValue.setEndpoints(list);
                    result.put(moduleUrl, newValue);
                }
            }
        }
        return result;
    }

    public List<GroupEntity> getAllParentGroups(GroupEntity group, List<GroupEntity> groups) {
        groups.add(group);
        GroupEntity parentGroup = group.getParentGroup();
        if (parentGroup != null) {
            getAllParentGroups(parentGroup, groups);
        }
        return groups;
    }

}
