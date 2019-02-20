package com.gliesereum.share.common.model.dto.file;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Data
@NoArgsConstructor
public class FileResponseDto {

    private Resource resource;

    private UserFileDto userFile;
}
