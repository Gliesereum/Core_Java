package com.gliesereum.share.common.model.dto.karma.record;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.base.description.DescriptionReadableDto;
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
public class RecordMessageDto extends DefaultDto {

    private String message;

    private DescriptionReadableDto<RecordMessageDescriptionDto> descriptions = new DescriptionReadableDto<>();
}