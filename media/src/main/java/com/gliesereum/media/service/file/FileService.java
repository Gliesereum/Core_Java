package com.gliesereum.media.service.file;

import com.gliesereum.share.common.model.dto.media.UserFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface FileService {

    UserFileDto uploadFile(UUID userId, MultipartFile multipartFile);
}
