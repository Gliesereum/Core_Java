package com.gliesereum.file.service.file;

import com.gliesereum.share.common.model.dto.file.UserFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface FileService {

    UserFileDto uploadFile(UUID userId, MultipartFile multipartFile);
}
