package com.gliesereum.account.service.user.impl;

import com.gliesereum.account.model.entity.UserPhoneEntity;
import com.gliesereum.account.model.repository.jpa.user.UserPhoneRepository;
import com.gliesereum.account.service.user.UserPhoneService;
import com.gliesereum.account.service.user.UserService;
import com.gliesereum.share.common.converter.DefaultConverter;
import com.gliesereum.share.common.exception.client.ClientException;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import com.gliesereum.share.common.service.DefaultServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

import static com.gliesereum.share.common.exception.messages.PhoneExceptionMessage.*;
import static com.gliesereum.share.common.exception.messages.UserExceptionMessage.*;

/**
 * @author vitalij
 * @since 10/10/2018
 */
@Service
public class UserPhoneServiceImpl extends DefaultServiceImpl<UserPhoneDto, UserPhoneEntity> implements UserPhoneService {

    @Autowired
    private UserPhoneRepository repository;

    @Autowired
    private DefaultConverter converter;

    @Autowired
    private UserService userService;

    public static final String PHONE_PATTERN = "\\+?[0-9]{6,14}";

    public static final Pattern phonePattern = Pattern.compile(PHONE_PATTERN);

    private static final Class<UserPhoneDto> DTO_CLASS = UserPhoneDto.class;
    private static final Class<UserPhoneEntity> ENTITY_CLASS = UserPhoneEntity.class;

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
    public UserPhoneDto getByUserId(UUID id) {
        UserPhoneDto result = null;
        if (id != null) {
            UserPhoneEntity user = repository.getByUserId(id);
            if (user != null) {
                result = converter.entityToDto(user, UserPhoneDto.class);
            }
        }
        return result;
    }

    @Override
    public void sendCode(String phone) {
        checkIsPhone(phone);
        //todo send code
    }

    @Override
    public UserPhoneDto update(String phone, String code) {
        //todo check code
        checkPhoneByExist(phone);
        //todo get userId by context
        UUID id = UUID.fromString(null);
        UserPhoneDto result = getByUserId(id);
        if (result == null) {
            throw new ClientException(PHONE_NOT_FOUND);
        }
        result.setPhone(phone);
        return update(result);
    }

    @Override
    public UserPhoneDto create(String phone, String code) {
        checkPhoneByExist(phone);
        //todo check code
        //todo get userId by context
        UUID id = UUID.fromString(null);
        UserDto user = userService.getById(id);
        if (user == null) {
            throw new ClientException(USER_NOT_FOUND);
        }
        UserPhoneDto result = new UserPhoneDto();
        result.setPhone(phone);
        result.setUser(user);
        return create(result);
    }

    @Override
    public boolean checkCode(String phone, String code) {
        return false;
    }

    private void checkPhoneByExist(String phone) {
        if (repository.existsUserPhoneEntityByPhone(phone)) {
            throw new ClientException(PHONE_EXIST);
        }
    }

    public void checkIsPhone(String str) {
        if (!phonePattern.matcher(str).matches()) {
            throw new ClientException(NOT_PHONE_BY_REGEX);
        }
    }
}
