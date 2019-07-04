package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.facade.auth.AuthFacade;
import com.gliesereum.account.facade.referral.ReferralFacade;
import com.gliesereum.account.model.entity.UserEntity;
import com.gliesereum.account.model.repository.jpa.user.UserRepository;
import com.gliesereum.account.service.user.CorporationSharedOwnershipService;
import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Slf4j
@Service
public class UserServiceImpl extends DefaultServiceImpl<UserDto, UserEntity> implements UserService {

    private static final String URL_PATTERN = "^(https:\\/\\/)[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
    private static final Pattern urlPattern = Pattern.compile(URL_PATTERN);
    private static final Class<UserDto> DTO_CLASS = UserDto.class;
    private static final Class<UserEntity> ENTITY_CLASS = UserEntity.class;

    private final UserRepository userRepository;

    @Autowired
    private UserEmailService emailService;

    @Autowired
    private UserPhoneService phoneService;

    @Autowired
    private CorporationSharedOwnershipService corporationSharedOwnershipService;

    @Autowired
    private ReferralFacade referralFacade;

    @Autowired
    private AuthFacade authFacade;

    public UserServiceImpl(UserRepository userRepository, DefaultConverter defaultConverter) {
        super(userRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (id != null) {
            emailService.deleteByUserId(id);
            phoneService.deleteByUserId(id);
            super.delete(id);
        }
    }

    @Override
    @Transactional
    public UserDto create(UserDto dto) {
        UserDto result = null;
        if (dto != null) {
            dto.setBanStatus(BanStatus.UNBAN);
            dto.setKycApproved(false);
            result = super.create(dto);
        }
        return result;
    }

    @Override
    @Transactional
    public UserDto create(UserDto user, String referralCode) {
        UserDto result = create(user);
        if ((result != null) && (StringUtils.isNotEmpty(referralCode))) {
            referralFacade.signUpWithCode(result.getId(), referralCode);
        }
        return result;
    }

    @Override
    @Transactional
    public UserDto updateMe(UserDto dto) {
        UserDto result = null;
        if (dto != null) {
            if (SecurityUtil.isAnonymous()) {
                throw new ClientException(USER_NOT_AUTHENTICATION);
            }
            dto.setId(SecurityUtil.getUserId());
            if (StringUtils.isNotEmpty(dto.getAvatarUrl()) && !urlPattern.matcher(dto.getAvatarUrl()).matches()) {
                throw new ClientException(UPL_AVATAR_IS_NOT_VALID);
            }
            if (StringUtils.isNotEmpty(dto.getCoverUrl()) && !urlPattern.matcher(dto.getCoverUrl()).matches()) {
                throw new ClientException(UPL_COVER_IS_NOT_VALID);
            }
            UserDto byId = super.getById(dto.getId());
            if (byId == null) {
                throw new ClientException(USER_NOT_FOUND);
            }
            dto.setBanStatus(byId.getBanStatus());
            dto.setLastActivity(byId.getLastActivity());
            dto.setLastSignIn(byId.getLastSignIn());
            dto.setCreateDate(byId.getCreateDate());
            result = super.update(dto);
            authFacade.tokenInfoUpdateEvent(Arrays.asList(result.getId()));
        }
        return result;
    }

    @Override
    public void banById(UUID id) {
        changeBanStatus(id, BanStatus.BAN);
    }

    @Override
    public void unBanById(UUID id) {
        changeBanStatus(id, BanStatus.UNBAN);
    }

    private void changeBanStatus(UUID id, BanStatus status) {
        UserDto user = getById(id);
        if (user == null) {
            throw new ClientException(USER_NOT_FOUND);
        }
        user.setBanStatus(status);
        super.update(user);
        authFacade.tokenInfoUpdateEvent(Arrays.asList(user.getId()));
    }

    @Override
    public UserDto getById(UUID id) {
        UserDto byId = super.getById(id);
        if (byId != null) {
            byId.setCorporationIds(corporationSharedOwnershipService.getAllCorporationIdByUserId(byId.getId()));
            byId.setPhone(phoneService.getPhoneByUserId(byId.getId()));
        }
        return byId;
    }

    @Override
    public List<UserDto> getByIds(Iterable<UUID> ids) {
        List<UserDto> byIds = super.getByIds(ids);
        if (CollectionUtils.isNotEmpty(byIds)) {
            List<UUID> userIds = IteratorUtils.toList(ids.iterator());
            Map<UUID, List<UUID>> corporationsIds = corporationSharedOwnershipService.getAllCorporationIdByUserIds(userIds);
            Map<UUID, String> phones = phoneService.getPhoneByUserIds(userIds);
            byIds.forEach(i -> {
                        if (MapUtils.isNotEmpty(phones)) {
                            i.setPhone(phones.get(i.getId()));
                        }
                        if (MapUtils.isNotEmpty(corporationsIds)) {
                            i.setCorporationIds(corporationsIds.get(i.getId()));
                        }
                    }
            );
        }
        return byIds;
    }

    @Override
    public void setKycApproved(UUID objectId) {
        UserDto user = super.getById(objectId);
        if (user == null) {
            throw new ClientException(USER_NOT_FOUND);
        }
        user.setKycApproved(true);
        super.update(user);
        authFacade.tokenInfoUpdateEvent(Arrays.asList(user.getId()));
    }

    @Override
    public UserDto getByPhone(String phone) {
        UserDto result = null;
        UserPhoneDto byPhone = phoneService.getByPhone(phone);
        if (byPhone != null) {
            result = getById(byPhone.getUserId());
        }
        return result;
    }

    @Override
    @Async
    public void updateAsync(UserDto user) {
        this.update(user);
    }
}
