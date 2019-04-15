package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.UserPhoneEntity;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 */
public interface UserPhoneService extends DefaultService<UserPhoneDto, UserPhoneEntity> {

    void deleteByUserId(UUID id);

    UserPhoneDto getByUserId(UUID id);

    UserPhoneDto getByPhone(String value);

    void sendCode(String phone);

    UserPhoneDto update(String phone, String code);

    UserPhoneDto create(String phone, String code);

    boolean checkPhoneByExist(String phone);
}
