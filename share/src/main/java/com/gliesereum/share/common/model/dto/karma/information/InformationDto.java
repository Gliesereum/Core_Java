package com.gliesereum.share.common.model.dto.karma.information;

import com.gliesereum.share.common.model.dto.DefaultDto;
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
public class InformationDto extends DefaultDto {

    private String title;

    private String description;

    private String videoUrl;

    private String imageUrl;

    private String tag;

    private Integer index;
}
