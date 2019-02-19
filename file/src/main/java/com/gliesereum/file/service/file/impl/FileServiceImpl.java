package com.gliesereum.file.service.file.impl;

import com.gliesereum.file.service.cdn.CdnService;
import com.gliesereum.file.service.file.FileService;
import com.gliesereum.file.service.user.UserFileService;
import com.gliesereum.share.common.exception.CustomException;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exception.messages.CommonExceptionMessage;
import com.gliesereum.share.common.model.dto.file.UserFileDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.MediaExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@Service
public class FileServiceImpl implements FileService {

    private static final String DEFAULT_FILENAME = "file";

    @Autowired
    private CdnService cdnService;

    @Autowired
    private UserFileService userFileService;

    @Value("${multipart.compatibleParentTypes}")
    private String[] compatibleParentTypes;

    @Override
    public UserFileDto uploadFile(UUID userId, MultipartFile multipartFile) {
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
            resultUrl = cdnService.uploadFile(filename, file);

        } finally {
            if (file != null && file.exists())
            file.delete();
        }
        return userFileService.create(new UserFileDto(filename, resultUrl, contentType, fileSize, userId));
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
}
