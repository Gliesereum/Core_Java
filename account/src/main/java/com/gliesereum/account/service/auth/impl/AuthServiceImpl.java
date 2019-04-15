package com.gliesereum.account.service.auth.impl;

import com.gliesereum.account.model.domain.TokenStoreDomain;
import com.gliesereum.account.service.auth.AuthService;
import com.gliesereum.account.service.token.TokenService;
import com.gliesereum.account.service.user.*;
import com.gliesereum.account.service.verification.VerificationService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.account.auth.SignInDto;
import com.gliesereum.share.common.model.dto.account.auth.TokenInfoDto;
import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.account.user.UserEmailDto;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.AuthExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.EmailExceptionMessage.EMAIL_EXIST;
import static com.gliesereum.share.common.exception.messages.EmailExceptionMessage.EMAIL_NOT_FOUND;
import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.PHONE_EXIST;
import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.PHONE_NOT_FOUND;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.USER_NOT_FOUND;

/**
 * @author yvlasiuk
 * @version 1.0
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private UserPhoneService phoneService;

    @Autowired
    private UserEmailService emailService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DefaultConverter defaultConverter;

    @Autowired
    private VerificationService verificationService;

    @Override
    @Transactional
    public AuthDto signIn(SignInDto signInDto) {
        AuthDto result = null;
        if (signInDto != null) {
            VerificationType type = signInDto.getType();
            String value = signInDto.getValue();
            if ((type.equals(VerificationType.EMAIL) && emailService.checkEmailByExist(value)) ||
                    (type.equals(VerificationType.PHONE) && phoneService.checkPhoneByExist(value))) {
                result = signIn(value, signInDto.getCode(), type);
            } else {
                result = signUp(value, signInDto.getCode(), type);
            }

        }
        return result;
    }

    @Transactional
    public AuthDto signIn(String value, String code, VerificationType type) {
        AuthDto result;
        UserDto user = null;
        if (verificationService.checkVerification(value, code)) {
            if (type.equals(VerificationType.EMAIL)) {
                UserEmailDto email = emailService.getByValue(value);
                user = userService.getById(email.getUserId());
            }
            if (type.equals(VerificationType.PHONE)){
                UserPhoneDto phone = phoneService.getByPhone(value);
                user = userService.getById(phone.getUserId());
            }
            if (user != null) {
                TokenStoreDomain token = tokenService.generate(user.getId().toString());
                result = createModel(token, user);
            } else {
                throw new ClientException(USER_NOT_FOUND);
            }
        } else {
            throw new ClientException(CODE_WORSE);
        }
        return result;
    }

    @Transactional
    public AuthDto signUp(String value, String code, VerificationType type) {
        AuthDto result = null;
        if (verificationService.checkVerification(value, code)) {
            checkValueByExist(value, type, true);
            UserDto newUser = userService.create(new UserDto());
            if (newUser != null) {
                if(type.equals(VerificationType.EMAIL)) {
                    UserEmailDto newEmail = new UserEmailDto();
                    newEmail.setEmail(value);
                    newEmail.setUserId(newUser.getId());
                    emailService.create(newEmail);
                }
                if (type.equals(VerificationType.PHONE)){
                    UserPhoneDto newPhone = new UserPhoneDto();
                    newPhone.setPhone(value);
                    newPhone.setUserId(newUser.getId());
                    phoneService.create(newPhone);
                }
                TokenStoreDomain token = tokenService.generate(newUser.getId().toString());
                result = createModel(token, newUser);
            }
        } else {
            throw new ClientException(CODE_WORSE);
        }
        return result;
    }

    //TODO: merge signin with signup
    /*@Override
    public AuthDto signIn(Map<String, String> params) {
        UserDto user = null;
        checkField(params);
        String value = params.get("value");
        String code = params.get("code");
        String type = params.get("type");
        VerificationType verificationType = VerificationType.valueOf(type);
        checkValueByExist(value, verificationType, false);
        if (verificationService.checkVerification(value, code)) {
            switch (verificationType) {
                case EMAIL: {
                    UserEmailDto email = emailService.getByValue(value);
                    user = userService.getById(email.getUserId());
                    break;
                }
                case PHONE: {
                    UserPhoneDto phone = phoneService.getByValue(value);
                    user = userService.getById(phone.getUserId());
                    break;
                }
            }
            if (user != null) {
                TokenStoreDomain token = tokenService.generate(user.getId().toString());
                return createModel(token, user);
            } else {
                throw new ClientException(USER_NOT_FOUND);
            }
        } else {
            throw new ClientException(CODE_WORSE);
        }
    }

    @Override
    @Transactional
    public AuthDto signUp(Map<String, String> params) {
        checkField(params);
        if (StringUtils.isEmpty(params.get("userType"))) {
            throw new ClientException(USER_TYPE_EMPTY);
        }
        String value = params.get("value");
        String code = params.get("code");
        String type = params.get("type");
        VerificationType verificationType = VerificationType.valueOf(type);
        if (verificationService.checkVerification(value, code)) {
            checkValueByExist(value, verificationType, true);
            UserDto newUser = userService.create(new UserDto());
            if (newUser != null) {
                switch (verificationType) {
                    case EMAIL: {
                        UserEmailDto newEmail = new UserEmailDto();
                        newEmail.setEmail(value);
                        newEmail.setUserId(newUser.getId());
                        emailService.create(newEmail);
                        break;
                    }
                    case PHONE: {
                        UserPhoneDto newPhone = new UserPhoneDto();
                        newPhone.setPhone(value);
                        newPhone.setUserId(newUser.getId());
                        phoneService.create(newPhone);
                        break;
                    }
                }
                TokenStoreDomain token = tokenService.generate(newUser.getId().toString());
                return createModel(token, newUser);
            }
        } else {
            throw new ClientException(CODE_WORSE);
        }
        return null;
    }*/

    @Override
    public AuthDto check(String accessToken) {
        TokenStoreDomain token = tokenService.getAndVerify(accessToken);
        UUID userId = UUID.fromString(token.getUserId());
        UserDto user = userService.getById(userId);
        if(user == null) {
            throw new ClientException(USER_NOT_FOUND);
        }
        return createModel(token, user);
    }

    private void checkField(Map<String, String> params) {
        if (StringUtils.isEmpty(params.get("value"))) {
            throw new ClientException(VALUE_EMPTY);
        }
        if (StringUtils.isEmpty(params.get("code"))) {
            throw new ClientException(CODE_EMPTY);
        }
        if (StringUtils.isEmpty(params.get("type"))) {
            throw new ClientException(TYPE_EMPTY);
        }
    }

    private void checkValueByExist(String value, VerificationType verificationType, boolean isNew) {
        if (verificationType.equals(VerificationType.PHONE)) {
            boolean exist = phoneService.checkPhoneByExist(value);
            if (isNew && exist) throw new ClientException(PHONE_EXIST);
            if (!isNew && !exist) throw new ClientException(PHONE_NOT_FOUND);
        }
        if (verificationType.equals(VerificationType.EMAIL)) {
            boolean exist = emailService.checkEmailByExist(value);
            if (isNew && exist) throw new ClientException(EMAIL_EXIST);
            if (!isNew && !exist) throw new ClientException(EMAIL_NOT_FOUND);
        }
    }

    private AuthDto createModel(TokenStoreDomain tokenStore, UserDto user) {
        AuthDto authDto = new AuthDto();
        authDto.setTokenInfo(defaultConverter.convert(tokenStore, TokenInfoDto.class));
        authDto.setUser(user);
        return authDto;
    }
}
