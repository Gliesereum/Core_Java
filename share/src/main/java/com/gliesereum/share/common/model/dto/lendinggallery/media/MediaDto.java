package com.gliesereum.share.common.model.dto.lendinggallery.media;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.lendinggallery.enumerated.BlockMediaType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private UUID objectId;

    private String title;

    private String description;

    private UUID fileId;

    @NotEmpty
    @Size(min = 8)
    private String url;

    @NotNull
    private BlockMediaType blockMediaType;
}
