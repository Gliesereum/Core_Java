package com.gliesereum.file.service.file;

import com.gliesereum.share.common.model.dto.file.FileResponseDto;
import com.gliesereum.share.common.model.dto.file.UserFileDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface FileService {

    FileResponseDto loadFile(UUID fileId, UUID userId);

    FileResponseDto loadPrivateFile(UUID fileId);

    UserFileDto uploadFile(UUID userId, UserFileDto userFile, MultipartFile multipartFile);

    void changeAccessToFile(UserFileDto userFile);

    void delete(String filename);

}
