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
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.account.user.UserEmailDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import com.gliesereum.share.common.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.gliesereum.share.common.exception.messages.AuthExceptionMessage.CODE_WORSE;
import static com.gliesereum.share.common.exception.messages.EmailExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_AUTHENTICATION;

/**
 * @author vitalij
 */
@Slf4j
@Service
public class UserEmailServiceImpl extends DefaultServiceImpl<UserEmailDto, UserEmailEntity> implements UserEmailService {

    private static final Class<UserEmailDto> DTO_CLASS = UserEmailDto.class;
    private static final Class<UserEmailEntity> ENTITY_CLASS = UserEmailEntity.class;

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);

    private final UserEmailRepository userEmailRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPhoneService phoneService;

    @Autowired
    private VerificationService verificationService;

    @Autowired
    public UserEmailServiceImpl(UserEmailRepository userEmailRepository, DefaultConverter defaultConverter) {
        super(userEmailRepository, defaultConverter, DTO_CLASS, ENTITY_CLASS);
        this.userEmailRepository = userEmailRepository;
    }


    @Override
    public void deleteByUserId(UUID id) {
        if (id != null) {
            userEmailRepository.deleteUserEmailEntityByUserId(id);
        }
    }

    @Override
    public void delete(UUID id) {
        if (id != null) {
            UUID userId = SecurityUtil.getUserId();
            checkUserAuthentication(userId);
            if (phoneService.getByUserId(userId) == null) {
                throw new ClientException(CAN_NOT_DELETE_EMAIL);
            }
            super.delete(id);
        }
    }

    @Override
    public UserEmailDto getByUserId(UUID id) {
        UserEmailDto result = null;
        if (id != null) {
            UserEmailEntity email = userEmailRepository.getByUserId(id);
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
            UserEmailEntity user = userEmailRepository.getUserEmailEntityByEmail(value);
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
        UserEmailDto result = null;
        UUID userId = SecurityUtil.getUserId();
        checkUserAuthentication(userId);
        if (verificationService.checkVerification(email, code)) {
            if (checkEmailByExist(email)) {
                throw new ClientException(EMAIL_EXIST);
            }
            result = getByUserId(userId);
            if (result == null) {
                throw new ClientException(USER_DOES_NOT_EMAIL);
            }
            result.setEmail(email);
        } else {
            throw new ClientException(CODE_WORSE);
        }
        return super.update(result);
    }

    @Override
    public UserEmailDto create(String email, String code) {
        UserEmailDto result = null;
        UUID userId = SecurityUtil.getUserId();
        checkUserAuthentication(userId);
        UserDto user = userService.getById(userId);
        if (verificationService.checkVerification(email, code)) {
            if (checkEmailByExist(email)) {
                throw new ClientException(EMAIL_EXIST);
            }
            if (getByUserId(userId) != null) {
                throw new ClientException(USER_ALREADY_HAS_EMAIL);
            }
            result = new UserEmailDto();
            result.setEmail(email);
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
    public boolean checkEmailByExist(String email) {
        checkIsEmail(email);
        return userEmailRepository.existsUserEmailEntityByEmail(email);
    }

    @Override
    public List<UserEmailDto> getByUserIds(List<UUID> ids) {
        List<UserEmailDto> result = null;
        if (CollectionUtils.isNotEmpty(ids)) {
            List<UserEmailEntity> entities = userEmailRepository.findByUserIdIn(ids);
            result = converter.convert(entities, dtoClass);
        }
        return result;
    }

    @Override
    public Map<UUID, UserEmailDto> getMapByUserIds(List<UUID> ids) {
        Map<UUID, UserEmailDto> result = new HashMap<>();
        if (CollectionUtils.isNotEmpty(ids)) {
            List<UserEmailDto> emails = getByUserIds(ids);
            if (CollectionUtils.isNotEmpty(emails)) {
                result = emails.stream().collect(Collectors.toMap(UserEmailDto::getUserId, i -> i));
            }
        }
        return result;
    }

    private void checkIsEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            throw new ClientException(EMAIL_EMPTY);
        }
        if (!emailPattern.matcher(email).matches()) {
            throw new ClientException(NOT_EMAIL_BY_REGEX);
        }
    }

    //TODO: Remove isNEW
    /*private void checkEmailForSignInUp(String email, boolean isNew) {
        checkIsEmail(email);
        if (!isNew && !checkEmailByExist(email)) throw new ClientException(EMAIL_NOT_FOUND);
        if (isNew && checkEmailByExist(email)) throw new ClientException(EMAIL_EXIST);
    }*/
}
