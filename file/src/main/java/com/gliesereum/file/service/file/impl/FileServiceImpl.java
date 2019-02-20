package com.gliesereum.file.service.file.impl;

import com.gliesereum.file.service.cdn.CdnService;
import com.gliesereum.file.service.file.FileService;
import com.gliesereum.file.service.user.UserFileService;
import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exception.messages.CommonExceptionMessage;
import com.gliesereum.share.common.model.dto.file.FileResponseDto;
import com.gliesereum.share.common.model.dto.file.UserFileDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.MediaExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private static final String DEFAULT_FILENAME = "file";

    @Autowired
    private CdnService cdnService;

    @Autowired
    private UserFileService userFileService;

    @Value("${multipart.compatibleParentTypes}")
    private String[] compatibleParentTypes;

    @Override
    public FileResponseDto loadFile(UUID fileId, UUID userId) {
        FileResponseDto fileResponse = null;
        if (ObjectUtils.allNotNull(fileId, userId)) {
            UserFileDto userFile = userFileService.getById(fileId);
            if (userFile == null) {
                throw new ClientException(USER_FILE_NOT_FOUND);
            }
            if (!userHaveAccessToFile(userFile, userId)) {
                throw new ClientException(CURRENT_USER_DONT_HAVE_ACCESS_TO_FILE);
            }
            Resource resource = cdnService.loadFile(userFile.getFilename());
            if (resource != null) {
                fileResponse = new FileResponseDto();
                fileResponse.setResource(resource);
                fileResponse.setUserFile(userFile);
            }
        }
        return fileResponse;
    }

    @Override
    public UserFileDto uploadFile(UUID userId, UserFileDto userFile, MultipartFile multipartFile) {
        String resultUrl = null;
        if ((multipartFile == null) || (multipartFile.isEmpty())) {
            throw new ClientException(MULTIPART_DATA_EMTPY);
        }
        String contentType = multipartFile.getContentType();
        if (StringUtils.isEmpty(contentType)) {
            throw new ClientException(MULTIPART_TYPE_UNDEFINED);
        }
        validContentType(contentType);
        long fileSize = multipartFile.getSize();
        String filename = generateFileName(multipartFile);
        File file = null;
        try {
            file = convertMultipartToFile(multipartFile);
            resultUrl = cdnService.uploadFile(filename, file, userFile.getOpen());

        } finally {
            if (file != null && file.exists()) {
                try {
                    Files.delete(file.toPath());
                } catch (IOException e) {
                    log.warn("Error while delete uploaded file", e);
                }
            }
        }
        return userFileService.create(insertFileInfo(userFile, filename, resultUrl, contentType, fileSize, userId));
    }

    @Override
    public void changeAccessToFile(UserFileDto userFile) {
        if ((userFile != null) && StringUtils.isNotEmpty(userFile.getFilename()) && (userFile.getOpen() != null)) {
            cdnService.changeAccessToFile(userFile.getFilename(), userFile.getOpen());
        }
    }

    @Override
    public void delete(String filename) {
        cdnService.delete(filename);
    }

    private UserFileDto insertFileInfo(UserFileDto userFile, String fileName, String fileUrl, String contentType, long fileSize, UUID userId) {
        userFile.setFilename(fileName);
        userFile.setUrl(fileUrl);
        userFile.setSize(fileSize);
        userFile.setUserId(userId);
        userFile.setMediaType(contentType);
        return userFile;
    }

    private void validContentType(String contentType) {
        boolean typeCompatible = false;
        MimeType mimeType = MimeType.valueOf(contentType);
        for (String compatibleParentType : compatibleParentTypes) {
            if (mimeType.getType().equals(compatibleParentType)) {
                typeCompatible = true;
                break;
            }
        }
        if (!typeCompatible) {
            throw new ClientException(MULTIPART_FILE_TYPE_NOT_COMPATIBLE);
        }
    }

    private String generateFileName(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        if (StringUtils.isEmpty(originalFilename)) {
            throw new ClientException(MULTIPART_FILE_NAME_UNDEFINED);
        }
        originalFilename = DEFAULT_FILENAME + originalFilename.substring(originalFilename.lastIndexOf('.'));

        return System.nanoTime() + "-" + originalFilename;
    }

    private File convertMultipartToFile(MultipartFile multipartFile) {
        File file;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)) {
                throw new ClientException(MULTIPART_FILE_NAME_UNDEFINED);
            }
            file = new File(originalFilename);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();
        } catch (Exception e) {
            throw new CustomException(CommonExceptionMessage.UNKNOWN_SERVER_EXCEPTION, e);
        }

        return file;
    }

    private boolean userHaveAccessToFile(UserFileDto userFile, UUID userId) {
        if (userFile.getUserId().equals(userId)) {
            return true;
        }
        if (CollectionUtils.isNotEmpty(userFile.getReaderIds()) && userFile.getReaderIds().contains(userId)) {
            return true;
        }
        return false;
    }
}
