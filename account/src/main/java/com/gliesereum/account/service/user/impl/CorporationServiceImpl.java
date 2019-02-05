package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.CorporationEntity;
import com.gliesereum.account.model.repository.jpa.user.CorporationRepository;
import com.gliesereum.account.service.depository.DepositoryService;
import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.account.service.user.CorporationSharedOwnershipService;
import com.gliesereum.account.service.user.UserCorporationService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exception.messages.ExceptionMessage;
import com.gliesereum.share.common.exchange.service.media.MediaExchangeService;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import com.gliesereum.share.common.model.dto.account.user.*;
import com.gliesereum.share.common.model.dto.media.UserFileDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.NOT_EXIST_BY_ID;
import static com.gliesereum.share.common.exception.messages.MediaExceptionMessage.UPLOAD_FAILED;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.*;

/**
 * @author vitalij
 */
@Slf4j
@Service
public class CorporationServiceImpl extends DefaultServiceImpl<CorporationDto, CorporationEntity> implements CorporationService {

    private static final Class<CorporationDto> DTO_CLASS = CorporationDto.class;
    private static final Class<CorporationEntity> ENTITY_CLASS = CorporationEntity.class;

    private final CorporationRepository corporationRepository;

    @Autowired
    private UserCorporationService userCorporationService;

    @Autowired
    private CorporationSharedOwnershipService sharedOwnershipService;

    @Autowired
    private UserService userService;

    @Autowired
    private DepositoryService depositoryService;

    @Autowired
    private MediaExchangeService mediaExchangeService;

    public CorporationServiceImpl(CorporationRepository corporationRepository, DefaultConverter converter) {
        super(corporationRepository, converter, DTO_CLASS, ENTITY_CLASS);
        this.corporationRepository = corporationRepository;
    }

    @Override
    @Transactional
    public CorporationDto create(CorporationDto dto) {
        CorporationDto result = null;
        SecurityUtil.checkUserByBanStatus();
        dto.setKYCStatus(KYCStatus.KYC_NOT_PASSED);
        dto.setVerifiedStatus(VerifiedStatus.UNVERIFIED);
        result = super.create(dto);
        if (result != null) {
            userCorporationService.create(new UserCorporationDto(SecurityUtil.getUserId(), result.getId())); //todo in a future remove user-corporation
            if (CollectionUtils.isNotEmpty(dto.getCorporationSharedOwnerships())) {
                if (dto.getCorporationSharedOwnerships().stream()
                        .peek(p -> checkCorporationSharedOwnerships(p))
                        .mapToInt(CorporationSharedOwnershipDto::getShare).sum() > 100) {
                    throw new ClientException(SUM_SHARE_MORE_THEN_100);
                }
                dto.getCorporationSharedOwnerships().forEach(f -> {
                    sharedOwnershipService.create(f);
                });
            } else {
                sharedOwnershipService.create(new CorporationSharedOwnershipDto(100, SecurityUtil.getUserId(), null, result.getId()));
            }
        }
        return result;
    }

    @Override
    public CorporationDto update(CorporationDto dto) {
        if (dto != null) {
            if (dto.getId() == null) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            checkCurrentUserForPermissionActionThisCorporation(dto.getId());
            CorporationDto byId = super.getById(dto.getId());
            if (byId == null) {
                throw new ClientException(NOT_EXIST_BY_ID);
            }
            checkByUpdateStatus(dto, byId);
            CorporationEntity entity = converter.convert(dto, entityClass);
            entity = corporationRepository.saveAndFlush(entity);
            dto = converter.convert(entity, dtoClass);
        }
        return dto;
    }

    @Override
    public void delete(UUID id) {
        checkCurrentUserForPermissionActionThisCorporation(id);
        super.delete(id);
    }

    @Override
    public void addOwnerCorporation(CorporationSharedOwnershipDto dto) {
        checkCorporationSharedOwnerships(dto);
        sharedOwnershipService.create(dto);
    }

    @Override
    public void removeOwnerCorporation(UUID id) {
        if (id == null) {
            throw new ClientException(CORPORATION_SHARED_OWNERSHIP_ID_IS_EMPTY);
        }
        CorporationSharedOwnershipDto dto = sharedOwnershipService.getById(id);
        if (dto == null) {
            throw new ClientException(CORPORATION_SHARED_OWNERSHIP_NOT_FOUND);
        }
        sharedOwnershipService.delete(id);
    }

    @Override
    public void updateKycStatus(KYCStatus status, UUID idCorporation) {
        if (status == null) {
            throw new ClientException(KYC_STATUS_IS_EMPTY);
        }
        CorporationDto corporation = getCorporationById(idCorporation);
        corporation.setKYCStatus(status);
        if (status.equals(KYCStatus.KYC_PASSED)) {
            corporation.setVerifiedStatus(VerifiedStatus.VERIFIED);
        } else {
            corporation.setVerifiedStatus(VerifiedStatus.UNVERIFIED);
        }
        super.update(corporation);
    }

    @Override
    public List<CorporationDto> getAllRequest() {
        List<CorporationEntity> entities = corporationRepository.findByKYCStatus(KYCStatus.KYC_IN_PROCESS);
        return converter.convert(entities, DTO_CLASS);
    }

    @Override
    public void uploadDocument(MultipartFile file, UUID idCorporation) {
        checkCurrentUserForPermissionActionThisCorporation(idCorporation);
        CorporationDto corporation = getById(idCorporation);
        if (corporation == null) {
            throw new ClientException(CORPORATION_NOT_FOUND);
        }
        encryptAndSave(file, idCorporation);
        if (!corporation.getKYCStatus().equals(KYCStatus.KYC_PASSED)) {
            corporation.setKYCStatus(KYCStatus.KYC_IN_PROCESS);
            super.update(corporation);
        }
    }

    @Override
    public void deleteDocument(String path, UUID idCorporation) {
        checkCurrentUserForPermissionActionThisCorporation(idCorporation);
        CorporationDto corporation = getById(idCorporation);
        if (corporation == null) {
            throw new ClientException(CORPORATION_NOT_FOUND);
        }
        mediaExchangeService.deleteFile(path);
    }

    private void checkCorporationSharedOwnerships(CorporationSharedOwnershipDto dto) {
        if (dto.getShare() <= 0 || dto.getShare() > 100) {
            throw new ClientException(WRONG_VALUE_SHARE);

        }
        if (dto.getCorporationOwnerId() == null && dto.getOwnerId() == null) {
            throw new ClientException(CORPORATION_OWNER_ID_AND_OWNER_ID_EMPTY);
        }
        if (dto.getOwnerId() != null) {
            UserDto user = userService.getById(dto.getOwnerId());
            if (user == null) {
                ExceptionMessage userNotFound = USER_NOT_FOUND;
                userNotFound.setMessage("User with id: " + dto.getOwnerId() + " not found");
                throw new ClientException(userNotFound);
            }
            if (user.getBanStatus().equals(BanStatus.BAN)) {
                ExceptionMessage userInBan = USER_IN_BAN;
                userInBan.setMessage("User with id: " + dto.getOwnerId() + " in ban status");
                throw new ClientException(userInBan);
            }
        }
        if (dto.getCorporationOwnerId() != null) {
            CorporationDto corporation = getById(dto.getCorporationOwnerId());
            if (corporation == null) {
                ExceptionMessage corporationNotFound = CORPORATION_NOT_FOUND;
                corporationNotFound.setMessage("Corporation with id: " + dto.getCorporationOwnerId() + " not found");
                throw new ClientException(corporationNotFound);
            }
            if (corporation.getVerifiedStatus().equals(VerifiedStatus.UNVERIFIED)) {
                ExceptionMessage corporationUnverified = CORPORATION_UNVERIFIED;
                corporationUnverified.setMessage("Corporation with id: " + dto.getCorporationOwnerId() + " unverified");
                throw new ClientException(corporationUnverified);
            }
        }
    }

    private void checkCurrentUserForPermissionActionThisCorporation(UUID id) {
        SecurityUtil.checkUserByBanStatus();
        UUID userId = SecurityUtil.getUserId();
        CorporationDto corporation = getCorporationById(id);
        List<CorporationSharedOwnershipDto> sharedOwnerships = corporation.getCorporationSharedOwnerships();
        if (CollectionUtils.isEmpty(sharedOwnerships) ||
                !sharedOwnerships.stream().map(CorporationSharedOwnershipDto::getOwnerId).collect(Collectors.toList()).contains(userId)) {
            throw new ClientException(USER_DONT_HAVE_PERMISSION_TO_CORPORATION);
        }
    }

    private CorporationDto getCorporationById(UUID id) {
        if (id == null) {
            throw new ClientException(CORPORATION_ID_IS_EMPTY);
        }
        CorporationDto corporation = getById(id);
        if (corporation == null) {
            throw new ClientException(CORPORATION_NOT_FOUND);
        }
        return corporation;
    }

    private void checkByUpdateStatus(CorporationDto dto, CorporationDto corporation) {
        if (corporation == null) {
            throw new ClientException(CORPORATION_NOT_FOUND);
        }
        if (!corporation.getKYCStatus().equals(dto.getKYCStatus())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_CHANGE_KYS_STATUS);
        }
        if (!corporation.getVerifiedStatus().equals(dto.getVerifiedStatus())) {
            throw new ClientException(DONT_HAVE_PERMISSION_TO_CHANGE_VERIFIED_STATUS);
        }
    }

    private void encryptAndSave(MultipartFile multipart, UUID idCorporation) {
        File file = null;
        try {
            file = multipartFileToFile(multipart);
            //todo encrypt, gzip file
            UserFileDto userFile = mediaExchangeService.uploadFile(file);
            if (userFile != null) {
                depositoryService.create(new DepositoryDto(userFile.getUrl(), idCorporation));
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
