package com.gliesereum.share.common.model.dto.karma.record;

import com.gliesereum.share.common.model.dto.base.description.BaseDescriptionDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author vitalij
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RecordMessageDescriptionDto extends BaseDescriptionDto {

    private String name;

    private String description;
}
