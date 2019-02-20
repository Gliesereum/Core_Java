package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.service.depository.DepositoryService;
import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.account.service.user.KycService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exchange.service.media.MediaExchangeService;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.DepositoryDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.file.UserFileDto;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.MediaExceptionMessage.UPLOAD_FAILED;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.KYC_STATUS_IS_EMPTY;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_AUTHENTICATION;

/**
 * @author vitalij
 */
@Slf4j
@Service
public class KycServiceImpl implements KycService {

    @Autowired
    private CorporationService corporationService;

    @Autowired
    private UserService userService;

    @Autowired
    private DepositoryService depositoryService;

    @Autowired
    private MediaExchangeService mediaExchangeService;

    @Override
    public void updateKycStatus(KYCStatus status, UUID objectId, boolean isCorporation) {
        if (status == null) {
            throw new ClientException(KYC_STATUS_IS_EMPTY);
        }
        if (isCorporation) {
            corporationService.updateKycStatus(objectId, status);
        } else {
            userService.updateKycStatus(objectId, status);
        }
    }

    @Override
    public List<CorporationDto> getAllCorporationKycRequest() {
        return corporationService.getAllKycRequest();
    }

    @Override
    public List<UserDto> getAllUserKycRequest() {
        return userService.getAllKycRequest();
    }

    @Override
    public void uploadDocument(MultipartFile file, UUID objectId, boolean isCorporation) {
        if (isCorporation) {
            corporationService.checkCurrentUserForPermissionActionThisCorporation(objectId);
            CorporationDto corporation = corporationService.getById(objectId);
            encryptAndSave(file, objectId);
            if (!corporation.getKYCStatus().equals(KYCStatus.KYC_PASSED)) {
                corporation.setKYCStatus(KYCStatus.KYC_IN_PROCESS);
                corporationService.update(corporation);
            }
        } else {
            if (SecurityUtil.isAnonymous()) {
                throw new ClientException(USER_NOT_AUTHENTICATION);
            }
            UserDto user = SecurityUtil.getUser().getUser();
            encryptAndSave(file, user.getId());
            if (!user.getKycStatus().equals(KYCStatus.KYC_PASSED)) {
                user.setKycStatus(KYCStatus.KYC_IN_PROCESS);
                userService.update(user);
            }
        }
    }

    @Override
    public void deleteDocument(String path, UUID objectId, boolean isCorporation) {
        if (isCorporation) {
            corporationService.checkCurrentUserForPermissionActionThisCorporation(objectId);
            CorporationDto corporation = corporationService.getById(objectId);
            corporation.setKYCStatus(KYCStatus.KYC_NOT_PASSED);
            corporationService.update(corporation);
            mediaExchangeService.deleteFile(path);
        } else {
            if (SecurityUtil.isAnonymous()) {
                throw new ClientException(USER_NOT_AUTHENTICATION);
            }
            UserDto user = SecurityUtil.getUser().getUser();
            user.setKycStatus(KYCStatus.KYC_NOT_PASSED);
            userService.update(user);
            mediaExchangeService.deleteFile(path);
        }
    }

    private void encryptAndSave(MultipartFile multipart, UUID ownerId) {
        File file = null;
        try {
            file = multipartFileToFile(multipart);
            //todo encrypt, gzip file
            UserFileDto userFile = mediaExchangeService.uploadFile(file);
            if (userFile != null) {
                depositoryService.create(new DepositoryDto(userFile.getUrl(), ownerId));
            } else {
                throw new ClientException(UPLOAD_FAILED);
            }
        } finally {
            if (file != null && file.exists())
                file.delete();
        }
    }

    private File multipartFileToFile(MultipartFile multipart) {
        File file = new File(multipart.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(file)) {
            file.createNewFile();
            fos.write(multipart.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

}
