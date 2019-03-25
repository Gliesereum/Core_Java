package com.gliesereum.share.common.model.dto.lendinggallery.media;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.BlockMediaType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author vitalij
 * @version 1.0
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MediaDto extends DefaultDto {

    private UUID objectId;

    @NotEmpty
    @Size(min = 2)
    private String title;

    private String description;

    private UUID fileId;

    private String url;

    private BlockMediaType blockMediaType;
}
