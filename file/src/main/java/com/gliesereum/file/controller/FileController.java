package com.gliesereum.file.controller;

import com.amazonaws.services.s3.model.ObjectListing;
import com.gliesereum.file.service.cdn.CdnService;
import com.gliesereum.file.service.file.FileService;
import com.gliesereum.file.service.user.UserFileService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.file.FileResponseDto;
import com.gliesereum.share.common.model.dto.file.UserFileDto;
import com.gliesereum.share.common.model.response.MapResponse;
import com.gliesereum.share.common.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.USER_IS_ANONYMOUS;

/**
 * @author yvlasiuk
 * @version 1.0
 */

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private CdnService cdnService;

    @Autowired
    private UserFileService userFileService;

    @PostMapping("/upload")
    public UserFileDto uploadFile(@RequestPart("file") MultipartFile multipartFile,
                                  @ModelAttribute UserFileDto userFile) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return fileService.uploadFile(SecurityUtil.getUserId(), userFile, multipartFile);
    }

    @GetMapping("/load/{fileId}")
    public ResponseEntity<Resource> loadFile(@PathVariable UUID fileId) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        FileResponseDto fileResponse = fileService.loadFile(fileId, SecurityUtil.getUserId());
        return getResourceResponseEntity(fileResponse);
    }

    @GetMapping("/load/private/{fileId}")
    public ResponseEntity<Resource> loadPrivateFile(@PathVariable UUID fileId) {
        FileResponseDto fileResponse = fileService.loadPrivateFile(fileId);
        return getResourceResponseEntity(fileResponse);
    }

    @GetMapping("/info/user")
    public List<UserFileDto> getAllForCurrentUser() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return userFileService.getByUserId(SecurityUtil.getUserId());
    }

    @GetMapping("/info/can-read")
    public List<UserFileDto> getAllCanRead() {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return userFileService.getAllCanRead(SecurityUtil.getUserId());
    }

    @PutMapping("/info")
    public UserFileDto updateInfo(@RequestBody UserFileDto userFile) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        return userFileService.updateCurrentUser(userFile);
    }

    @DeleteMapping("/info/{fileId}")
    public MapResponse deleteUserFile(@PathVariable UUID fileId) {
        if (SecurityUtil.isAnonymous()) {
            throw new ClientException(USER_IS_ANONYMOUS);
        }
        userFileService.deleteCurrentUser(fileId);
        return new MapResponse("true");
    }

    @DeleteMapping("/uploaded")
    public MapResponse deleteUploaded(@RequestParam("key") String key) {
        cdnService.deleteByKey(key);
        return new MapResponse(true);
    }

    @GetMapping("/uploaded")
    public ObjectListing getAllUploaded() {
        return cdnService.getAllFiles();
    }

    private ResponseEntity<Resource> getResourceResponseEntity(FileResponseDto fileResponse) {
        ResponseEntity<Resource> result;
        if (fileResponse != null) {
            Resource resource = fileResponse.getResource();
            UserFileDto userFile = fileResponse.getUserFile();

            result = ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(userFile.getMediaType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + userFile.getFilename() + "\"")
                    //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userFile.getFilename() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(userFile.getSize()))
                    .body(resource);

        } else {
            result = ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return result;
    }

}
