package com.gliesereum.share.common.model.dto.karma.business;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.BusinessType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BusinessCategoryDto extends DefaultDto {

    @NotEmpty
    private String code;

    @NotEmpty
    private String name;

    private String description;

    private String imageUrl;

    @NotNull
    private BusinessType businessType;

    private boolean active;

    private Integer orderIndex;

    private DescriptionReadableDto<BusinessCategoryDescriptionDto> descriptions = new DescriptionReadableDto<>();

}
