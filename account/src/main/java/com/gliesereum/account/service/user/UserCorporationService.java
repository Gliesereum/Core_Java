package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.UserCorporationEntity;
import com.gliesereum.share.common.model.dto.account.user.UserCorporationDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
public interface UserCorporationService extends DefaultService<UserCorporationDto, UserCorporationEntity> {

    List<UserCorporationDto> getAllByUserIdAndCorporationId(UUID userId, List<UUID> corporationIds);
}
