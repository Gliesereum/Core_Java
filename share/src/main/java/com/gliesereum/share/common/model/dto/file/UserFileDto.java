package com.gliesereum.share.common.model.dto.file;

import com.gliesereum.share.common.model.dto.DefaultDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserFileDto extends DefaultDto {

    private String filename;

    private String originalFilename;

    private String url;

    private String mediaType;

    private Long size;

    private UUID userId;

    @NotNull
    private Boolean open;

    private Boolean crypto;

    private List<String> keys;

    private List<UUID> readerIds;

    public UserFileDto(UserFileDto userFile) {
        this.filename = userFile.getFilename();
        this.url = userFile.getUrl();
        this.mediaType = userFile.getMediaType();
        this.size = userFile.getSize();
        this.userId = userFile.getUserId();
        this.open = userFile.getOpen();
        this.crypto = userFile.getCrypto();
        this.keys = userFile.getKeys();
        this.readerIds = userFile.getReaderIds();
    }
}
