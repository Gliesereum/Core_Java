package com.gliesereum.account.service;

import com.gliesereum.account.model.entity.UserEmailEntity;
import com.gliesereum.share.common.model.dto.account.UserEmailDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @since 10/10/2018
 */
public interface UserEmailService extends DefaultService<UserEmailDto, UserEmailEntity> {

    void deleteByUserId(UUID id);
}
