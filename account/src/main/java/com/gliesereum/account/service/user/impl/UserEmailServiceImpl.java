package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserEmailEntity;
import com.gliesereum.account.model.repository.jpa.user.UserEmailRepository;
import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.account.service.verification.VerificationService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;
import com.gliesereum.share.common.model.dto.account.enumerated.VerifiedStatus;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.account.user.UserEmailDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.regex.Pattern;

import static com.gliesereum.share.common.exception.messages.AuthExceptionMessage.CODE_WORSE;
import static com.gliesereum.share.common.exception.messages.EmailExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_FOUND;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@Service
public class UserEmailServiceImpl extends DefaultServiceImpl<UserEmailDto, UserEmailEntity> implements UserEmailService {

    @Autowired
    private UserEmailRepository repository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPhoneService phoneService;

    @Autowired
    private VerificationService verificationService;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    private static final Class<UserEmailDto> DTO_CLASS = UserEmailDto.class;
    private static final Class<UserEmailEntity> ENTITY_CLASS = UserEmailEntity.class;

    public UserEmailServiceImpl(UserEmailRepository userEmailRepository, DefaultConverter defaultConverter) {
        super(userEmailRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
    }


    @Override
    public void deleteByUserId(UUID id) {
        if (id != null) {
            repository.deleteUserEmailEntityByUserId(id);
        }
    }

    @Override
    public void delete(UUID id) {
        if (id != null) {
            //todo get userId by context
            UUID userId = UUID.fromString(null);
            UserDto user = userService.getById(userId);
            if (user == null) {
                throw new ClientException(USER_NOT_FOUND);
            }
            if (phoneService.getByUserId(userId) == null) {
                throw new ClientException(CAN_NOT_DELETE_EMAIL);
            }
            super.delete(id);
            updateUserStatus(user, VerifiedStatus.UNVERIFIED);
        }
    }

    @Override
    public UserEmailDto getByUserId(UUID id) {
        UserEmailDto result = null;
        if (id != null) {
            UserEmailEntity email = repository.getByUserId(id);
            if (email != null) {
                result = converter.convert(email, UserEmailDto.class);
            }
        }
        return result;
    }

    @Override
    public UserEmailDto getByValue(String value) {
        UserEmailDto result = null;
        if (!StringUtils.isEmpty(value)) {
            UserEmailEntity user = repository.getUserEmailEntityByEmail(value);
            if (user != null) {
                result = converter.convert(user, UserEmailDto.class);
            }
        }
        return result;
    }

    @Override
    public void sendCode(String email) {
        checkIsEmail(email);
        verificationService.sendVerificationCode(email, VerificationType.EMAIL);
    }

    @Override
    public UserEmailDto update(String email, String code) {
        if (verificationService.checkVerification(email, code)) {
            if (checkEmailByExist(email)) {
                throw new ClientException(EMAIL_EXIST);
            }
            //todo get userId by context
            UUID id = UUID.fromString(null);
            UserDto user = userService.getById(id);
            if (user == null) {
                throw new ClientException(USER_NOT_FOUND);
            }
            UserEmailDto result = getByUserId(user.getId());
            if (result == null) {
                throw new ClientException(USER_DOES_NOT_EMAIL);
            }
            result.setEmail(email);
            return update(result);
        } else {
            throw new ClientException(CODE_WORSE);
        }
    }

    @Override
    public UserEmailDto create(String email, String code) {
        if (verificationService.checkVerification(email, code)) {
            if (checkEmailByExist(email)) {
                throw new ClientException(EMAIL_EXIST);
            }
            //todo get userId by context
            UUID id = UUID.fromString(null);
            UserDto user = userService.getById(id);
            if (user == null) {
                throw new ClientException(USER_NOT_FOUND);
            }
            if (getByUserId(id) != null) {
                throw new ClientException(USER_ALREADY_HAS_EMAIL);
            }
            UserEmailDto result = new UserEmailDto();
            result.setEmail(email);
            result.setUser(user);
            updateUserStatus(user, VerifiedStatus.VERIFIED);
            return create(result);
        } else {
            throw new ClientException(CODE_WORSE);
        }
    }

    @Override
    public boolean checkEmailByExist(String email) {
        checkIsEmail(email);
        return repository.existsUserEmailEntityByEmail(email);
    }

    public void checkIsEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new ClientException(EMAIL_EMPTY);
        }
        if (!emailPattern.matcher(email).matches()) {
            throw new ClientException(NOT_EMAIL_BY_REGEX);
        }
    }

    private void updateUserStatus(UserDto user, VerifiedStatus status) {
        user.setVerifiedStatus(status);
        userService.update(user);
    }
}
