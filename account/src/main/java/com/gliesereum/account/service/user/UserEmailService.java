package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.UserEmailEntity;
import com.gliesereum.share.common.model.dto.account.user.UserEmailDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
public interface UserEmailService extends DefaultService<UserEmailDto, UserEmailEntity> {

    void deleteByUserId(UUID id);

    UserEmailDto getByUserId(UUID id);

    UserEmailDto getByValue(String value);

    void sendCode(String email);

    UserEmailDto update(String email, String code);

    UserEmailDto create(String email, String code);

    boolean checkEmailByExist(String email);
}
