package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.facade.auth.UserUpdateFacade;
import com.gliesereum.account.facade.notification.SystemNotificationFacade;
import com.gliesereum.account.model.entity.CorporationEntity;
import com.gliesereum.account.model.repository.jpa.user.CorporationRepository;
import com.gliesereum.account.service.kyc.KycRequestService;
import com.gliesereum.account.service.user.CorporationService;
import com.gliesereum.account.service.user.CorporationSharedOwnershipService;
import com.gliesereum.account.service.user.UserCorporationService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.exception.messages.ExceptionMessage;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.model.dto.account.user.UserCorporationDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.enumerated.ObjectState;
import com.gliesereum.share.common.service.auditable.impl.AuditableServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.ID_NOT_SPECIFIED;
import static com.gliesereum.share.common.exception.messages.CommonExceptionMessage.NOT_EXIST_BY_ID;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.*;

/**
 * @author vitalij
 */
@Slf4j
@Service
public class CorporationServiceImpl extends AuditableServiceImpl<CorporationDto, CorporationEntity> implements CorporationService {

    private static final Class<CorporationDto> DTO_CLASS = CorporationDto.class;
    private static final Class<CorporationEntity> ENTITY_CLASS = CorporationEntity.class;

    private final CorporationRepository corporationRepository;

    @Value("${image-url.corporation.logo}")
    private String defaultCorporationLogo;

    @Value("${image-url.corporation.cover}")
    private String defaultCorporationCover;

    @Autowired
    private UserCorporationService userCorporationService;

    @Autowired
    private CorporationSharedOwnershipService sharedOwnershipService;

    @Autowired
    private UserService userService;

    @Autowired
    private KycRequestService kycRequestService;

    @Autowired
    private SystemNotificationFacade systemNotificationFacade;

    @Autowired
    private UserUpdateFacade userUpdateFacade;

    @Autowired
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
        setLogoIfNull(dto);
        result = super.create(dto);
        if (result != null) {
            userCorporationService.create(new UserCorporationDto(SecurityUtil.getUserId(), result.getId())); //todo in a future remove user-corporation
            if (CollectionUtils.isNotEmpty(dto.getCorporationSharedOwnerships())) {
                if (dto.getCorporationSharedOwnerships().stream()
                        .peek(this::checkCorporationSharedOwnerships)
                        .mapToInt(CorporationSharedOwnershipDto::getShare).sum() > 100) {
                    throw new ClientException(SUM_SHARE_MORE_THEN_100);
                }
                List<UUID> userIds = dto.getCorporationSharedOwnerships().stream().map(sharedOwnershipService::create).map(CorporationSharedOwnershipDto::getOwnerId).collect(Collectors.toList());
                userUpdateFacade.tokenInfoUpdateEvent(userIds);
            } else {
                sharedOwnershipService.create(new CorporationSharedOwnershipDto(100, SecurityUtil.getUserId(), null, result.getId()));
                userUpdateFacade.tokenInfoUpdateEvent(Arrays.asList(SecurityUtil.getUserId()));
            }
        }
        return result;
    }

    @Override
    public List<CorporationDto> getAll() {
        List<CorporationEntity> entities = corporationRepository.findAllByObjectStateOrderByName(ObjectState.ACTIVE);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public CorporationDto update(CorporationDto dto) {
        if (dto != null) {
            if (dto.getId() == null) {
                throw new ClientException(ID_NOT_SPECIFIED);
            }
            setLogoIfNull(dto);
            checkCurrentUserForPermissionActionThisCorporation(dto.getId());
            Optional<CorporationEntity> byId = corporationRepository.findByIdAndObjectState(dto.getId(), ObjectState.ACTIVE);
            if (byId.isEmpty()) {
                throw new ClientException(NOT_EXIST_BY_ID);
            }
            dto.setKycApproved(byId.get().getKycApproved());
            dto = super.update(dto);
        }
        return dto;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        checkCurrentUserForPermissionActionThisCorporation(id);
        CorporationDto dto = getById(id);
        super.delete(dto);
        kycRequestService.delete(KycRequestType.CORPORATION, id);
        systemNotificationFacade.sendCorporationDelete(dto);
    }

    @Override
    public void addOwnerCorporation(CorporationSharedOwnershipDto dto) {
        checkCorporationSharedOwnerships(dto);
        CorporationSharedOwnershipDto owner = sharedOwnershipService.create(dto);
        userUpdateFacade.tokenInfoUpdateEvent(Arrays.asList(owner.getOwnerId()));
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
        Optional<CorporationEntity> entity = corporationRepository.findByIdAndObjectState(id, ObjectState.ACTIVE);
        if (entity.isEmpty()) {
            throw new ClientException(CORPORATION_NOT_FOUND);
        }
        return converter.convert(entity.get(), dtoClass);
    }

    @Override
    public List<CorporationDto> getByUserId(UUID userId) {
        List<CorporationDto> result = null;
        List<UUID> corporationIds = sharedOwnershipService.getAllCorporationIdByUserId(userId);
        if (CollectionUtils.isNotEmpty(corporationIds)) {
            List<CorporationEntity> entities = corporationRepository.findAllByIdInAndObjectState(corporationIds, ObjectState.ACTIVE);
            if (CollectionUtils.isNotEmpty(entities)) {
                result = converter.convert(entities, dtoClass);

                result.sort(Comparator.comparing(i -> (i.getName() != null) ? i.getName() : ""));
            }
        }
        return result;
    }

    @Override
    public void setKycApproved(UUID objectId) {
        CorporationDto corporation = getById(objectId);
        corporation.setKycApproved(true);
        super.update(corporation);
    }

    @Override
    public Map<UUID, List<CorporationDto>> getCorporationByUserIds(List<UUID> userIds) {
        Map<UUID, List<CorporationDto>> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<CorporationSharedOwnershipDto> ownerships = sharedOwnershipService.getAllByUserIds(userIds);
            if (CollectionUtils.isNotEmpty(ownerships)) {
                List<UUID> corporationIds = ownerships.stream()
                        .map(CorporationSharedOwnershipDto::getCorporationId)
                        .collect(Collectors.toList());
                List<CorporationDto> corporations = super.getByIds(corporationIds);
                if (CollectionUtils.isNotEmpty(corporations)) {
                    Map<UUID, CorporationDto> corporationMap = corporations.stream()
                            .collect(Collectors.toMap(CorporationDto::getId, i -> i));
                    ownerships.stream().forEach(i -> {
                        List<CorporationDto> value = result.getOrDefault(i.getOwnerId(), new ArrayList<>());
                        value.add(corporationMap.get(i.getCorporationId()));
                        result.put(i.getOwnerId(), value);
                    });
                }
            }
        }
        return result;
    }

    private void setLogoIfNull(CorporationDto corporation) {
        if (corporation != null) {
            if (corporation.getLogoUrl() == null) {
                corporation.setLogoUrl(defaultCorporationLogo);
            }
            if (corporation.getCoverUrl() == null) {
                corporation.setCoverUrl(defaultCorporationCover);
            }
        }
    }
}
