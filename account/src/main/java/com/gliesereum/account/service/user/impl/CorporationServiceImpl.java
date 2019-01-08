package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.CorporationEntity;
import com.gliesereum.account.model.repository.jpa.user.CorporationRepository;
import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.account.service.user.UserCorporationService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.UserCorporationDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.*;

/**
 * @author vitalij
 * @since 12/4/18
 */
@Slf4j
@Service
public class CorporationServiceImpl extends DefaultServiceImpl<CorporationDto, CorporationEntity> implements CorporationService {

    private static final Class<CorporationDto> DTO_CLASS = CorporationDto.class;
    private static final Class<CorporationEntity> ENTITY_CLASS = CorporationEntity.class;

    public CorporationServiceImpl(CorporationRepository repository, DefaultConverter converter) {
        super(repository, converter, DTO_CLASS, ENTITY_CLASS);
    }

    @Autowired
    private UserCorporationService userCorporationService;

    @Autowired
    private UserService userservice;

    @Override
    public CorporationDto create(CorporationDto dto) {
        CorporationDto result = null;
        checkUserByStatus();
        dto.setKYCStatus(KYCStatus.KYC_NOT_PASSED);
        dto.setVerifiedStatus(VerifiedStatus.UNVERIFIED);
        result = super.create(dto);
        if (result != null) {
            userCorporationService.create(new UserCorporationDto(SecurityUtil.getUserId(), result.getId()));
        }
        return result;
    }

    @Override
    public CorporationDto update(CorporationDto dto) {
        checkCurrentUserForPermissionActionThisCorporation(dto.getId());
        return super.update(dto);
    }

    @Override
    public void delete(UUID id) {
        checkCurrentUserForPermissionActionThisCorporation(id);
        super.delete(id);
    }

    @Override
    public void addUser(UUID idCorporation, UUID idUser) {
        checkActionWithUserFromCorporation(idCorporation, idUser);
        userCorporationService.create(new UserCorporationDto(idUser, idCorporation));
    }

    @Override
    public void removeUser(UUID idCorporation, UUID idUser) {
        checkActionWithUserFromCorporation(idCorporation, idUser);
        UserCorporationDto userCorporation = userCorporationService.getByUserIdAndCorporationId(idUser, idCorporation);
        if (userCorporation == null) {
            throw new ClientException(DONT_FOUND_DEPENDENCY_USER_AND_CORPORATION);
        }
        userCorporationService.delete(userCorporation.getId());
    }

    private void checkActionWithUserFromCorporation(UUID idCorporation, UUID idUser) {
        checkUserByStatus();
        checkCurrentUserForPermissionActionThisCorporation(idCorporation);
        if (userservice.getById(idUser) == null) {
            throw new ClientException(USER_NOT_FOUND);
        }
    }

    private void checkUserByStatus() {
        if (SecurityUtil.getUser() == null) {
            throw new ClientException(USER_NOT_AUTHENTICATION);
        }
        UserDto user = SecurityUtil.getUser().getUser();
        if (user.getBanStatus().equals(BanStatus.BAN)) {
            throw new ClientException(USER_IN_BAN);
        }
    }

    private void checkCurrentUserForPermissionActionThisCorporation(UUID id) {
        UUID userId = SecurityUtil.getUserId();
        UserCorporationDto userCorporation = userCorporationService.getByUserIdAndCorporationId(userId, id);
        if (userCorporation == null) {
            throw new ClientException(USER_DONT_HAVE_PERMISSION_TO_CORPORATION);
        }
    }


}
