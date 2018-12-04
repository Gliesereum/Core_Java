package com.gliesereum.share.common.model.dto.permission.endpoint;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.base.enumerated.Method;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/11/2018
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EndpointDto extends DefaultDto {

    private String title;

    private String description;

    private String url;

    private Method method;

    private String version;

    private boolean isActive;

    private String inactiveMessage;

    private UUID moduleId;
}
