package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.CorporationEntity;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
public interface CorporationService extends DefaultService<CorporationDto, CorporationEntity> {

    void addOwnerCorporation(CorporationSharedOwnershipDto dto);

    void removeOwnerCorporation(UUID id);

    void updateKycStatus(UUID id, KYCStatus status);

    List<CorporationDto> getAllKycRequest();

    void checkCurrentUserForPermissionActionThisCorporation(UUID id);
}
