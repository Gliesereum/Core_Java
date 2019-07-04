package com.gliesereum.share.common.model.dto.karma.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.gliesereum.share.common.databind.json.description.DescriptionJsonDeserializer;
import com.gliesereum.share.common.databind.json.description.DescriptionJsonSerializer;
import com.gliesereum.share.common.model.dto.karma.business.descriptions.BusinessDescriptionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vitalij
 * @version 1.0
 * @since 12/7/18
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class BaseBusinessDto extends AbstractBusinessDto {

    private List<WorkTimeDto> workTimes = new ArrayList<>();

    private List<WorkingSpaceDto> spaces = new ArrayList<>();

    @JsonDeserialize(using = DescriptionJsonDeserializer.class)
    @JsonSerialize(using = DescriptionJsonSerializer.class)
    private List<BusinessDescriptionDto> descriptions = new ArrayList<>();

}
