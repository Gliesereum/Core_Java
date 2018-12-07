package com.gliesereum.share.common.model.dto.karma.media;

import com.gliesereum.share.common.model.dto.DefaultDto;
import com.gliesereum.share.common.model.dto.karma.enumerated.MediaType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-08
 */

@Data
@NoArgsConstructor
public class MediaDto extends DefaultDto {

    private UUID objectId;

    private String title;

    private String description;

    private String url;

    private MediaType mediaType;
}
