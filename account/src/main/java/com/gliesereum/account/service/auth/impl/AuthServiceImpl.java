package com.gliesereum.account.service.auth.impl;

import com.gliesereum.account.model.domain.TokenStoreDomain;
import com.gliesereum.account.service.auth.AuthService;
import com.gliesereum.account.service.token.TokenService;
import com.gliesereum.account.service.user.UserEmailService;
import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.account.service.verification.VerificationService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.auth.AuthDto;
import com.gliesereum.share.common.model.dto.account.auth.TokenInfoDto;
import com.gliesereum.share.common.model.dto.account.enumerated.VerificationType;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.account.user.UserEmailDto;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;

import static com.gliesereum.share.common.exception.messages.AuthExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.EmailExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.*;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 11/10/2018
 */
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
    public AuthDto signin(Map<String, String> params) {
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
                    user = email.getUser();
                    break;
                }
                case PHONE: {
                    UserPhoneDto phone = phoneService.getByValue(value);
                    user = phone.getUser();
                    break;
                }
            }
            if (user != null) {
                TokenStoreDomain token = tokenService.generate(user.getId().toString());
                return createModel(token, user);
            } else {
                throw new ClientException(USER_NOT_FOUND);
            }
        }
        return null;
    }

    @Override
    public AuthDto signup(Map<String, String> params) {
        checkField(params);
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
                        newEmail.setUser(newUser);
                        emailService.create(newEmail);
                        break;
                    }
                    case PHONE: {
                        UserPhoneDto newPhone = new UserPhoneDto();
                        newPhone.setPhone(value);
                        newPhone.setUser(newUser);
                        phoneService.create(newPhone);
                        break;
                    }
                }
                TokenStoreDomain token = tokenService.generate(newUser.getId().toString());
                return createModel(token, newUser);
            }
        }
        return null;
    }

    @Override
    public AuthDto check(String accessToken) {
        TokenStoreDomain token = tokenService.getAndVerify(accessToken);
        UserDto user = userService.getById(UUID.fromString(token.getUserId()));
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
        switch (verificationType) {
            case PHONE: {
                boolean exist = phoneService.checkPhoneByExist(value);
                if (isNew && exist) throw new ClientException(PHONE_EXIST);
                if (!isNew && !exist) throw new ClientException(PHONE_NOT_FOUND);
                break;
            }
            case EMAIL: {
                boolean exist = emailService.checkEmailByExist(value);
                if (isNew && exist) throw new ClientException(EMAIL_EXIST);
                if (!isNew && !exist) throw new ClientException(EMAIL_NOT_FOUND);
                break;
            }
        }
    }

    private AuthDto createModel(TokenStoreDomain tokenStore, UserDto user) {
        AuthDto authDto = new AuthDto();
        authDto.setTokenInfo(defaultConverter.convert(tokenStore, TokenInfoDto.class));
        authDto.setUser(user);
        return authDto;
    }
}
