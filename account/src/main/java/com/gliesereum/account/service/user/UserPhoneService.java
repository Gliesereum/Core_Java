package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.UserPhoneEntity;
import com.gliesereum.share.common.model.dto.account.user.UserPhoneDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
public interface UserPhoneService extends DefaultService<UserPhoneDto, UserPhoneEntity> {

    void deleteByUserId(UUID id);

    UserPhoneDto getByUserId(UUID id);

    void sendCode(String phone);
}
