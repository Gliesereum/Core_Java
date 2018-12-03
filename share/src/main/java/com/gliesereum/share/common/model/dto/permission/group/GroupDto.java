package com.gliesereum.share.common.model.dto.permission.group;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/11/2018
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GroupDto extends DefaultDto {

    private String title;

    private String description;

    private String purpose;

    private boolean isActive;

    private String inactiveMessage;

    private UUID parentGroupId;

    private GroupDto parentGroup;

    private List<EndpointDto> endpoints = new ArrayList<>();

    private List<GroupUserDto> users = new ArrayList<>();
}
