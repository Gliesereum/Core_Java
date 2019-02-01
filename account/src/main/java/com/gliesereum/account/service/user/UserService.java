package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.UserEntity;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface UserService extends DefaultService<UserDto, UserEntity> {

    void banById(UUID id);

    void unBanById(UUID id);

    UserDto updateMe(UserDto dto);
}
