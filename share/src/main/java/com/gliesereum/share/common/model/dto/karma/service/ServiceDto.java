package com.gliesereum.share.common.model.dto.karma.service;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
import com.gliesereum.share.common.model.dto.karma.service.descriptions.ServiceDescriptionDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ServiceDto extends DefaultDto {

    @NotEmpty
    private String name;

    @NotEmpty
    private String description;

    private UUID businessCategoryId;

    private ObjectState objectState;

    private DescriptionReadableDto<ServiceDescriptionDto> descriptions = new DescriptionReadableDto<>();

}
