package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.UserEmailEntity;
import com.gliesereum.share.common.model.dto.account.user.UserEmailDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 */
public interface UserEmailService extends DefaultService<UserEmailDto, UserEmailEntity> {

    void deleteByUserId(UUID id);

    UserEmailDto getByUserId(UUID id);

    UserEmailDto getByValue(String value);

    void sendCode(String email, Boolean devMode);

    UserEmailDto update(String email, String code);

    UserEmailDto create(String email, String code);

    boolean checkEmailByExist(String email);

    List<UserEmailDto> getByUserIds(List<UUID> ids);

    Map<UUID, UserEmailDto> getMapByUserIds(List<UUID> ids);
}
