package com.gliesereum.share.common.model.dto.karma.media;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.MediaType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MediaDto extends DefaultDto {

    @NotNull
    private UUID objectId;

    @NotEmpty
    @Size(min = 2)
    private String title;

    private String description;

    @NotEmpty
    @Size(min = 8)
    private String url;

    @NotNull
    private MediaType mediaType;
}
