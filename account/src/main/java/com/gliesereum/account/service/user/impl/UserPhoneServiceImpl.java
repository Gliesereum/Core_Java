package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserPhoneEntity;
import com.gliesereum.account.model.repository.jpa.user.UserPhoneRepository;
import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.account.service.verification.VerificationService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.RegexUtil;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

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

    @Autowired
    private UserPhoneRepository repository;

    @Autowired
    private DefaultConverter converter;

    @Autowired
    private UserService userService;

    @Autowired
    private UserEmailService emailService;

    @Autowired
    private VerificationService verificationService;

    public UserPhoneServiceImpl(UserPhoneRepository userPhoneRepository, DefaultConverter defaultConverter) {
        super(userPhoneRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }


    @Override
    public void deleteByUserId(UUID id) {
        if (id != null) {
            repository.deleteUserPhoneEntityByUserId(id);
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
            UserPhoneEntity phone = repository.getByUserId(id);
            if (phone != null) {
                result = converter.convert(phone, UserPhoneDto.class);
            }
        }
        return result;
    }

    @Override
    public UserPhoneDto getByPhone(String value) {
        UserPhoneDto result = null;
        checkIsPhone(value);
        UserPhoneEntity user = repository.getUserPhoneEntityByPhone(value);
        if (user != null) {
            result = converter.convert(user, UserPhoneDto.class);
        }
        return result;
    }

    @Override
    public void sendCode(String phone) {
        //TODO: REMOVE isNEW
        //checkPhoneForSignInUp(phone, isNew);
        checkIsPhone(phone);
        verificationService.sendVerificationCode(phone, VerificationType.PHONE);
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
            UserDto user = userService.getById(userId);
            if (getByUserId(userId) != null) {
                throw new ClientException(USER_ALREADY_HAS_PHONE);
            }
            result = new UserPhoneDto();
            result.setPhone(phone);
            result.setUserId(user.getId());
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
        return repository.existsUserPhoneEntityByPhone(phone);
    }

    @Override
    public List<UserPhoneDto> getByUserIds(List<UUID> ids) {
        List<UserPhoneEntity> entities = repository.getByUserIdIn(ids);
        return converter.convert(entities,dtoClass);
    }

    public void checkIsPhone(String phone) {
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
