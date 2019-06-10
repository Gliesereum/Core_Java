package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.CorporationEntity;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 */
public interface CorporationService extends DefaultService<CorporationDto, CorporationEntity> {

    void addOwnerCorporation(CorporationSharedOwnershipDto dto);

    void removeOwnerCorporation(UUID id);

    void checkCurrentUserForPermissionActionThisCorporation(UUID id);

    void setKycApproved(UUID objectId);

    List<CorporationDto> getByUserId(UUID userId);

    Map<UUID, List<CorporationDto>> getCorporationByUserIds(List<UUID> userIds);
}
