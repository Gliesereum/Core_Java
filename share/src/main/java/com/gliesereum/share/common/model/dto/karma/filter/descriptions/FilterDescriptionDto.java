package com.gliesereum.share.common.model.dto.karma.filter.descriptions;

import com.gliesereum.share.common.model.dto.base.description.BaseDescriptionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class FilterDescriptionDto extends BaseDescriptionDto {

    private String title;
}
