package com.gliesereum.account.service.user;

import com.gliesereum.share.common.model.dto.account.enumerated.KYCStatus;
import com.gliesereum.share.common.model.dto.account.user.CorporationDto;
import com.gliesereum.share.common.model.dto.account.user.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

/**
 * @author vitalij
 */
public interface KycService {

    void updateKycStatus(KYCStatus status, UUID objectId, boolean isCorporation);
    //TODO: KYC REFACTOR
   // List<CorporationDto> getAllCorporationKycRequest();

    //List<UserDto> getAllUserKycRequest();

//    void uploadDocument(MultipartFile file, UUID objectId, boolean isCorporation);
//
//    void deleteDocument(String path, UUID objectId, boolean isCorporation);

}
