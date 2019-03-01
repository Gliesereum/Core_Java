package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.CorporationEntity;
import com.gliesereum.account.model.repository.jpa.user.CorporationRepository;
import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.account.service.user.CorporationSharedOwnershipService;
import com.gliesereum.account.service.user.UserCorporationService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exception.messages.ExceptionMessage;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.model.dto.account.user.UserCorporationDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.NOT_EXIST_BY_ID;
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

    public CorporationServiceImpl(CorporationRepository corporationRepository, DefaultConverter converter) {
        super(corporationRepository, converter, DTO_CLASS, ENTITY_CLASS);
        this.corporationRepository = corporationRepository;
    }

    @Override
    @Transactional
    public CorporationDto create(CorporationDto dto) {
        CorporationDto result = null;
        SecurityUtil.checkUserByBanStatus();
        dto.setKycApproved(false);
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
            dto.setKycApproved(byId.getKycApproved());
            CorporationEntity entity = converter.convert(dto, entityClass);
            entity = corporationRepository.saveAndFlush(entity);
            dto = converter.convert(entity, dtoClass);
        }
        return dto;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        checkCurrentUserForPermissionActionThisCorporation(id);
        CorporationDto dto = getById(id);
        dto.setObjectState(ObjectState.DELETED);
        super.update(dto);
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

    @Override
    public void checkCurrentUserForPermissionActionThisCorporation(UUID id) {
        SecurityUtil.checkUserByBanStatus();
        UUID userId = SecurityUtil.getUserId();
        CorporationDto corporation = getById(id);
        List<CorporationSharedOwnershipDto> sharedOwnerships = corporation.getCorporationSharedOwnerships();
        if (CollectionUtils.isEmpty(sharedOwnerships) ||
                sharedOwnerships.stream().noneMatch(i -> i.getOwnerId().equals(userId))) {
            throw new ClientException(USER_DONT_HAVE_PERMISSION_TO_CORPORATION);
        }
    }

    @Override
    public CorporationDto getById(UUID id) {
        if (id == null) {
            throw new ClientException(CORPORATION_ID_IS_EMPTY);
        }
        CorporationDto corporation = super.getById(id);
        if (corporation == null) {
            throw new ClientException(CORPORATION_NOT_FOUND);
        }
        return corporation;
    }

    @Override
    public void setKycApproved(UUID objectId) {
        CorporationDto corporation = getById(objectId);
        corporation.setKycApproved(true);
        super.update(corporation);
    }
}
