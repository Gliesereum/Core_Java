package com.gliesereum.share.common.model.dto.karma.business;

import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
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

    private DescriptionReadableDto<BusinessDescriptionDto> descriptions = new DescriptionReadableDto<>();

}
