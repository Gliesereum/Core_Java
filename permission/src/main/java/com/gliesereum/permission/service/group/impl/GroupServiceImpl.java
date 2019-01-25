package com.gliesereum.permission.service.group.impl;

import com.gliesereum.permission.model.entity.endpoint.EndpointEntity;
import com.gliesereum.permission.model.entity.group.GroupEntity;
import com.gliesereum.permission.model.entity.module.ModuleEntity;
import com.gliesereum.permission.model.repository.jpa.group.GroupRepository;
import com.gliesereum.permission.service.group.GroupService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.account.UserExchangeService;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import com.gliesereum.share.common.model.dto.permission.enumerated.GroupPurpose;
import com.gliesereum.share.common.model.dto.permission.group.GroupDto;
import com.gliesereum.share.common.model.dto.permission.module.ModuleDto;
import com.gliesereum.share.common.model.dto.permission.permission.PermissionMapValue;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
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
    private UserExchangeService userExchangeService;

    @Autowired
    public GroupServiceImpl(GroupRepository groupRepository, DefaultConverter defaultConverter) {
        super(groupRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.groupRepository = groupRepository;
    }

    @Override
    public GroupDto getDefaultGroup(UserDto user) {
        GroupDto result = null;
        if (user.getBanStatus().equals(BanStatus.BAN)) {
            result = getByPurpose(GroupPurpose.BANNED);
        } else if (userExchangeService.userKYCPassed(user.getId())){
            result = getByPurpose(GroupPurpose.KYC_PASSED);
        } else {
            result = getByPurpose(GroupPurpose.AUTH);
        }
        return result;
    }

    @Override
    public GroupDto getForAnonymous() {
        return getByPurpose(GroupPurpose.ANONYMOUS);
    }

    @Override
    public GroupDto getByPurpose(GroupPurpose purpose) {
        GroupDto result = null;
        if (purpose != null) {
            Optional<GroupEntity> entityOptional = groupRepository.findByPurpose(purpose);
            if (entityOptional.isPresent()) {
                result = converter.convert(entityOptional.get(), dtoClass);
            }
        }
        return result;
    }

    @Override
    public Map<String, PermissionMapValue> getPermissionMap(UUID groupId) {
        if (groupId == null) {
            throw new ClientException(ID_NOT_SPECIFIED);
        }
        Optional<GroupEntity> groupOptional = repository.findById(groupId);
        GroupEntity group = groupOptional.orElseThrow(() -> new ClientException(GROUP_NOT_FOUND));
        List<GroupEntity> allParentGroups = getAllParentGroups(group, new ArrayList<>());

        return convertToMap(allParentGroups);
    }

    public Map<String, PermissionMapValue> convertToMap(List<GroupEntity> groups) {
        Map<String, PermissionMapValue> result = null;
        if (CollectionUtils.isNotEmpty(groups)) {
            result = new HashMap<>();
            Set<EndpointEntity> endpointEntities = groups.stream()
                    .flatMap(i -> i.getEndpoints().stream())
                    .collect(Collectors.toSet());

            for (EndpointEntity endpoint: endpointEntities) {
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

    public List<GroupEntity> getAllParentGroups(GroupEntity group, List<GroupEntity> groupIds) {
        groupIds.add(group);
        GroupEntity parentGroup = group.getParentGroup();
        if (parentGroup != null) {
            getAllParentGroups(parentGroup, groupIds);
        }
        return groupIds;
    }

}
