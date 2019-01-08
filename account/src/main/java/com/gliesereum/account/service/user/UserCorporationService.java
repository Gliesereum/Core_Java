package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.UserCorporationEntity;
import com.gliesereum.share.common.model.dto.account.user.UserCorporationDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.UUID;

/**
 * @author vitalij
 * @since 12/4/18
 */
public interface UserCorporationService extends DefaultService<UserCorporationDto, UserCorporationEntity> {

    UserCorporationDto getByUserIdAndCorporationId(UUID userId, UUID corporationId);
}
