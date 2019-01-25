package com.gliesereum.share.common.model.dto.permission.endpoint;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.base.enumerated.Method;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EndpointDto extends DefaultDto {

    @NotEmpty
    @Size(min = 2)
    private String title;

    private String description;

    @NotEmpty
    private String url;

    @NotNull
    private Method method;

    private String version;

    private boolean isActive;

    private String inactiveMessage;

    @NotNull
    private UUID moduleId;
}
