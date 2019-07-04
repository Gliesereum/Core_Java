package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.CorporationSharedOwnershipEntity;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author vitalij
 */
public interface CorporationSharedOwnershipService extends DefaultService<CorporationSharedOwnershipDto, CorporationSharedOwnershipEntity> {

    List<CorporationSharedOwnershipDto> getAllByUserId(UUID id);

    List<CorporationSharedOwnershipDto> getAllByCorporationOwnerId(UUID id);

    List<UUID> getAllCorporationIdByUserId(UUID id);

    Map<UUID, List<UUID>> getAllCorporationIdByUserIds(List<UUID> userIds);

    List<CorporationSharedOwnershipDto> getAllByUserIds(List<UUID> userIds);
}
