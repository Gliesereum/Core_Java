package com.gliesereum.share.common.model.dto.permission.permission;

import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import com.gliesereum.share.common.model.dto.permission.module.ModuleDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
public class PermissionMapValue {

    private ModuleDto module;

    private List<EndpointDto> endpoints;
}
