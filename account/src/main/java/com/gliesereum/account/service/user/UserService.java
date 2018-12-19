package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.UserEntity;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 * @since 08/10/2018
 */
public interface UserService extends DefaultService<UserDto, UserEntity> {

    void banById(UUID id);

    UserDto updateWithOutCheckModel(UserDto dto);
}
