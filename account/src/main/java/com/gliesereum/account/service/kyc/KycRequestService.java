package com.gliesereum.account.service.kyc;

import com.gliesereum.account.model.entity.kyc.KycRequestEntity;
import com.gliesereum.share.common.model.dto.account.enumerated.KycRequestType;
import com.gliesereum.share.common.model.dto.account.enumerated.KycStatus;
import com.gliesereum.share.common.model.dto.account.kyc.KycRequestFullModelDto;
import com.gliesereum.share.common.model.dto.account.kyc.KycValuesRequestDto;
import com.gliesereum.share.common.model.dto.account.kyc.KycRequestDto;
import com.gliesereum.share.common.service.DefaultService;

import java.util.List;
import java.util.UUID;

/**
 * @author yvlasiuk
 * @version 1.0
 */
public interface KycRequestService extends DefaultService<KycRequestDto, KycRequestEntity> {

    KycRequestDto createRequest(KycValuesRequestDto createRequest);

    KycRequestDto updateRequest(KycValuesRequestDto updateRequest);

    boolean openKycRequestExist(UUID objectId, KycRequestType requestType);

    KycRequestDto updateStatus(UUID id, KycStatus newStatus, String comment);

    List<KycRequestDto> getAllByUserId(UUID userId);

    List<KycRequestFullModelDto> getFullModelAll(List<KycStatus> statuses);

    void delete(KycRequestType requestType, UUID objectId);
}
