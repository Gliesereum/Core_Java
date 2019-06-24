package com.gliesereum.share.common.model.dto.karma.business;

import com.gliesereum.share.common.model.dto.base.description.BaseDescriptionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkingSpaceDescriptionDto extends BaseDescriptionDto {

    private String name;

    private String description;
}
