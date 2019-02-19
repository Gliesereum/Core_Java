package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserEntity;
import com.gliesereum.account.model.repository.jpa.user.UserRepository;
import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.BanStatus;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public static final Pattern urlPattern = Pattern.compile(URL_PATTERN);
    private static final Class<UserDto> DTO_CLASS = UserDto.class;
    private static final Class<UserEntity> ENTITY_CLASS = UserEntity.class;
    private final UserRepository userRepository;
    @Autowired
    private UserEmailService emailService;
    @Autowired
    private UserPhoneService phoneService;

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
            dto.setKycStatus(KYCStatus.KYC_NOT_PASSED);
            result = super.create(dto);
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
            if (StringUtils.isEmpty(dto.getAvatarUrl()) && !urlPattern.matcher(dto.getAvatarUrl()).matches()) {
                throw new ClientException(UPL_AVATAR_IS_NOT_VALID);
            }
            if (StringUtils.isEmpty(dto.getCoverUrl()) && !urlPattern.matcher(dto.getCoverUrl()).matches()) {
                throw new ClientException(UPL_COVER_IS_NOT_VALID);
            }
            UserDto byId = super.getById(dto.getId());
            if (byId == null) {
                throw new ClientException(USER_NOT_FOUND);
            }
            dto.setBanStatus(byId.getBanStatus());
            result = super.update(dto);
        }
        return result;
    }

    @Override
    public void updateKycStatus(UUID id, KYCStatus status) {
        UserDto user = getById(id);
        if (user == null) {
            throw new ClientException(USER_NOT_FOUND);
        }
        user.setKycStatus(user.getKycStatus());
        super.update(user);
    }

    @Override
    public void banById(UUID id) {
        changeBanStatus(id, BanStatus.BAN);
    }

    @Override
    public void unBanById(UUID id) {
        changeBanStatus(id, BanStatus.UNBAN);
    }

    @Override
    public List<UserDto> getAllKycRequest() {
        List<UserEntity> entities = userRepository.findByKycStatus(KYCStatus.KYC_IN_PROCESS);
        return converter.convert(entities, DTO_CLASS);
    }

    private void changeBanStatus(UUID id, BanStatus status) {
        UserDto user = getById(id);
        if (user == null) {
            throw new ClientException(USER_NOT_FOUND);
        }
        user.setBanStatus(status);
        super.update(user);
    }
}
