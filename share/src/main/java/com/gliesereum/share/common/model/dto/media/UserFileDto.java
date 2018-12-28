package com.gliesereum.share.common.model.dto.media;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-28
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFileDto extends DefaultDto {

    private String filename;

    private String url;

    private String mediaType;

    private Long size;

    private UUID userId;
}
