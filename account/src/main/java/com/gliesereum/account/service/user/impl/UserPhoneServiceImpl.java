package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserPhoneEntity;
import com.gliesereum.account.model.repository.jpa.user.UserPhoneRepository;
import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.account.service.verification.VerificationService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.RegexUtil;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.AuthExceptionMessage.CODE_WORSE;
import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_AUTHENTICATION;

/**
 * @author vitalij
 */
@Slf4j
@Service
public class UserPhoneServiceImpl extends DefaultServiceImpl<UserPhoneDto, UserPhoneEntity> implements UserPhoneService {

    private static final Class<UserPhoneDto> DTO_CLASS = UserPhoneDto.class;
    private static final Class<UserPhoneEntity> ENTITY_CLASS = UserPhoneEntity.class;

    private final UserPhoneRepository userPhoneRepository;

    @Autowired
    private UserEmailService emailService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    public UserPhoneServiceImpl(UserPhoneRepository userPhoneRepository, DefaultConverter defaultConverter) {
        super(userPhoneRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.userPhoneRepository = userPhoneRepository;
    }

    @Override
    public void deleteByUserId(UUID id) {
        if (id != null) {
            userPhoneRepository.deleteUserPhoneEntityByUserId(id);
        }
    }

    @Override
    public void delete(UUID id) {
        UUID userId = SecurityUtil.getUserId();
        checkUserAuthentication(userId);
        if (id != null) {
            if (emailService.getByUserId(userId) == null) {
                throw new ClientException(CAN_NOT_DELETE_PHONE);
            }
            super.delete(id);
        }
    }

    @Override
    public UserPhoneDto getByUserId(UUID id) {
        UserPhoneDto result = null;
        if (id != null) {
            UserPhoneEntity phone = userPhoneRepository.getByUserId(id);
            if (phone != null) {
                result = converter.convert(phone, UserPhoneDto.class);
            }
        }
        return result;
    }

    @Override
    public String getPhoneByUserId(UUID id) {
        String result = null;
        if (id != null) {
            UserPhoneEntity userPhone = userPhoneRepository.getByUserId(id);
            if (userPhone != null) {
                result = userPhone.getPhone();
            }
        }
        return result;
    }

    @Override
    public UserPhoneDto getByPhone(String value) {
        UserPhoneDto result = null;
        checkIsPhone(value);
        UserPhoneEntity user = userPhoneRepository.getUserPhoneEntityByPhone(value);
        if (user != null) {
            result = converter.convert(user, UserPhoneDto.class);
        }
        return result;
    }

    @Override
    public void sendCode(String phone, Boolean devMode) {
        //TODO: REMOVE isNEW
        //checkPhoneForSignInUp(phone, isNew);
        checkIsPhone(phone);
        verificationService.sendVerificationCode(phone, VerificationType.PHONE, devMode);
    }

    @Override
    public UserPhoneDto update(String phone, String code) {
        UserPhoneDto result = null;
        UUID userId = SecurityUtil.getUserId();
        checkUserAuthentication(userId);
        if (verificationService.checkVerification(phone, code)) {
            if (checkPhoneByExist(phone)) {
                throw new ClientException(PHONE_EXIST);
            }
            result = getByUserId(userId);
            if (result == null) {
                throw new ClientException(USER_DOES_NOT_PHONE);
            }
            result.setPhone(phone);
        } else {
            throw new ClientException(CODE_WORSE);
        }
        return super.update(result);
    }

    @Override
    public UserPhoneDto create(String phone, String code) {
        UserPhoneDto result = null;
        UUID userId = SecurityUtil.getUserId();
        checkUserAuthentication(userId);
        if (verificationService.checkVerification(phone, code)) {
            if (checkPhoneByExist(phone)) {
                throw new ClientException(PHONE_EXIST);
            }
            if (getByUserId(userId) != null) {
                throw new ClientException(USER_ALREADY_HAS_PHONE);
            }
            result = new UserPhoneDto();
            result.setPhone(phone);
            result.setUserId(userId);
        } else {
            throw new ClientException(CODE_WORSE);
        }
        return super.create(result);
    }

    private void checkUserAuthentication(UUID userId) {
        if (userId == null) {
            throw new ClientException(USER_NOT_AUTHENTICATION);
        }
    }

    @Override
    public boolean checkPhoneByExist(String phone) {
        checkIsPhone(phone);
        return userPhoneRepository.existsUserPhoneEntityByPhone(phone);
    }

    @Override
    public List<UserPhoneDto> getByUserIds(List<UUID> ids) {
        List<UserPhoneEntity> entities = userPhoneRepository.getByUserIdIn(ids);
        return converter.convert(entities, dtoClass);
    }

    @Override
    public Map<UUID, UserPhoneDto> getMapByUserIds(List<UUID> ids) {
        Map<UUID, UserPhoneDto> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            List<UserPhoneDto> phones = getByUserIds(ids);
            if (CollectionUtils.isNotEmpty(phones)) {
                result = phones.stream().collect(Collectors.toMap(UserPhoneDto::getUserId, i -> i));
            }
        }
        return result;
    }

    @Override
    public Map<UUID, String> getPhoneByUserIds(List<UUID> ids) {
        Map<UUID, String> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            List<UserPhoneDto> phones = getByUserIds(ids);
            if (CollectionUtils.isNotEmpty(phones)) {
                result = phones.stream().collect(Collectors.toMap(UserPhoneDto::getUserId, UserPhoneDto::getPhone));
            }
        }
        return result;
    }

    private void checkIsPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            throw new ClientException(PHONE_EMPTY);
        }
        if (!RegexUtil.phoneIsValid(phone)) {
            throw new ClientException(NOT_PHONE_BY_REGEX);
        }
    }

    /* TODO: REMOVE isNEW
    private void checkPhoneForSignInUp(String phone, boolean isNew) {
        checkIsPhone(phone);
        if (!isNew && !checkPhoneByExist(phone)) throw new ClientException(PHONE_NOT_FOUND);
        if (isNew && checkPhoneByExist(phone)) throw new ClientException(PHONE_EXIST);
    }*/
}
