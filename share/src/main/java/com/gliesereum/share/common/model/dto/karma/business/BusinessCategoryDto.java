package com.gliesereum.share.common.model.dto.karma.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.description.DescriptionJsonDeserializer;
import com.gliesereum.share.common.databind.json.description.DescriptionJsonSerializer;
import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.business.descriptions.BusinessCategoryDescriptionDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.BusinessType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    @JsonDeserialize(using = DescriptionJsonDeserializer.class)
    @JsonSerialize(using = DescriptionJsonSerializer.class)
    private List<BusinessCategoryDescriptionDto> descriptions = new ArrayList<>();

}
