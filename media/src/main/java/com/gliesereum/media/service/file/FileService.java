package com.gliesereum.media.service.file;

import com.gliesereum.share.common.model.dto.media.UserFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 2018-12-27
 */
public interface FileService {

    UserFileDto uploadFile(UUID userId, MultipartFile multipartFile);
}
