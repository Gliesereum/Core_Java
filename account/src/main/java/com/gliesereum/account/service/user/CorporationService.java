package com.gliesereum.account.service.user;

import com.gliesereum.account.model.entity.CorporationEntity;
import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.CorporationSharedOwnershipDto;
import com.gliesereum.share.common.service.DefaultService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
public interface CorporationService extends DefaultService<CorporationDto, CorporationEntity> {

    void updateKycStatus(KYCStatus status, UUID idCorporation);

    List<CorporationDto> getAllRequest();

    void uploadDocument(MultipartFile file, UUID idCorporation);

    void deleteDocument(String path, UUID idCorporation);

    void addOwnerCorporation(CorporationSharedOwnershipDto dto);

    void removeOwnerCorporation(UUID id);
}
