package com.gliesereum.share.common.model.dto.permission.module;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.permission.endpoint.EndpointDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/11/2018
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ModuleDto extends DefaultDto {

    @NotEmpty
    private String title;

    private String description;

    @NotEmpty
    private String url;

    @NotEmpty
    private String version;

    private boolean isActive;

    private String inactiveMessage;

    private List<EndpointDto> endpoints = new ArrayList<>();
}
