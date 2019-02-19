package com.gliesereum.share.common.model.dto.file;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
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

    private Boolean open;

    private Boolean crypto;

    private List<String> keys;

    private List<String> readerIds;

    public UserFileDto(String filename, String resultUrl, String contentType, long fileSize, UUID userId) {
        this.filename = filename;
        this.url = resultUrl;
        this.mediaType = contentType;
        this.size = fileSize;
        this.userId = userId;
    }
}
